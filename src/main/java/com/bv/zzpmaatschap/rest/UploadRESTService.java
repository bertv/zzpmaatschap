package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.pricelist.PriceList;
import com.bv.zzpmaatschap.model.report.Report;
import com.bv.zzpmaatschap.services.PriceListImportService;
import com.bv.zzpmaatschap.services.ReportFileService;
import com.bv.zzpmaatschap.services.ReportGenerator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


@Stateless
@Path("/upload")
public class UploadRESTService {

    public static final String JASPER_REPORTS_COMPILED_EXTENSION = ".jasper";
    public static final String JASPER_REPORTS_TEMPLATE_EXTENSION = ".jrxml";
    @EJB
    public PriceListImportService priceListImportService;

    @EJB
    public ReportGenerator reportGenerator;

    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;

    @EJB
    private ReportFileService reportFileService;

    public static final String FILENAME_HEADER = "filename=\"";

    @POST
    @Path("/report")
    @Consumes("multipart/form-data")
    public Response uploadReport(@Context HttpServletRequest request) {
        String status = "";

        try {
            FileMultiParts fileMultiParts = new FileMultiParts(request).invoke();
            Long offerId = fileMultiParts.getOfferId();
            InputStream inputStream = fileMultiParts.getInputStream();
            String filename = fileMultiParts.getFilename();

            Offer offer = offerEAO.find(Offer.class, offerId);
            OutputStream outputStream = new ByteArrayOutputStream();
            File tempFile = createTempFile(outputStream, inputStream);


            if (filename.endsWith(JASPER_REPORTS_COMPILED_EXTENSION)) {

                reportFileService.registerReportFile(filename.replace(JASPER_REPORTS_COMPILED_EXTENSION, ""), new FileInputStream(tempFile), offer, false);
                status = "ok";
            } else if (filename.endsWith(JASPER_REPORTS_TEMPLATE_EXTENSION)) {
                Report report = reportFileService.registerReportFile(filename.replace(JASPER_REPORTS_TEMPLATE_EXTENSION, ""), new FileInputStream(tempFile), offer, true);
                reportFileService.extractParametersFromTemplate(filename.replace(JASPER_REPORTS_TEMPLATE_EXTENSION, ""), new FileInputStream(tempFile), report);
                status = "ok";
            }

        } catch (IOException e) {
            status = "error";
        } catch (ServletException e) {
            status = "error";
        }


        return createResponse(status, "/app2/generate.html?status=");

    }

    private Response createResponse(String status, String redirectLocation) {
        URI uri = null;
        try {

            uri = new URI(redirectLocation + status);
        } catch (URISyntaxException e) {

        }
        return Response.temporaryRedirect(uri).build();
    }


    @POST
    @Path("/pricelist")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest request) {

        String status = "";
        try {
            FileMultiParts fileMultiParts = new FileMultiParts(request).invoke();

            InputStream inputStream = fileMultiParts.getInputStream();
            String filename = fileMultiParts.getFilename();
            if (filename.endsWith(".csv")) {

                OutputStream outputStream = null;
                File tempFile = createTempFile(outputStream, inputStream);
                PriceList priceList = priceListImportService.createPricelist(filename);
                priceListImportService.countItems(new FileInputStream(tempFile), priceList);
                priceListImportService.importFile(new FileInputStream(tempFile), priceList);

                status = "ok";
            } else {
                status = "error_fe";//file not existing.
            }

        } catch (Exception e) {
            status = "error";
        }


        return createResponse(status, "/app2/uploadpricelist.html?status=");
    }


    public File createTempFile(OutputStream outputStream, InputStream inputStream) throws IOException {
        File tempFile;
        try {
            tempFile = File.createTempFile("sdd", "dfdf");
            OutputStream fileOutputStream =
                    new FileOutputStream(tempFile);
            writeToOutputStream(fileOutputStream, inputStream);
        } finally {

            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return tempFile;
    }

    public void writeToOutputStream(OutputStream outputStream, InputStream inputStream) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

    }


    private class FileMultiParts {
        private HttpServletRequest request;
        private String filename;
        private Long offerId;
        private InputStream inputStream;

        public FileMultiParts(HttpServletRequest request) {
            this.request = request;
        }

        public String getFilename() {
            return filename;
        }

        public Long getOfferId() {
            return offerId;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public FileMultiParts invoke() throws IOException, ServletException {
            filename = extractFileName(request.getPart("uploadedFile"));
            OutputStream stringOutputStream = new ByteArrayOutputStream();
            if (request.getPart("offerid")!=null){
            writeToOutputStream(stringOutputStream, request.getPart("offerid").getInputStream());
            offerId = Long.valueOf(stringOutputStream.toString());
            }
            inputStream = request.getPart("uploadedFile").getInputStream();
            return this;
        }

        /**
         * Extracts file name from HTTP header content-disposition
         */
        private String extractFileName(Part part) {
            String contentDisp = part.getHeader("content-disposition");
            String[] items = contentDisp.split(";");
            for (String s : items) {
                if (s.trim().startsWith("filename")) {
                    return s.substring(s.indexOf("=") + 2, s.length() - 1);
                }
            }
            return "";
        }
    }
}

package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.eao.inferface.IReportEAO;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.report.Report;
import com.bv.zzpmaatschap.services.ReportFileService;
import com.bv.zzpmaatschap.services.ReportGenerator;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


@RolesAllowed({"inactive", "admin", "user_temp"})
@Stateless
@Path("/report")
public class ReportRESTService {

    @EJB
    public ReportGenerator reportGenerator;


    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;
    @EJB
    private ReportFileService reportFileService;

    @EJB(beanName = "reportEAO", beanInterface = IReportEAO.class)
    IReportEAO reportEAO;


    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Report> getAllReportFiles(@QueryParam("offerid") Long id) {
        return reportFileService.getReports(null, id);
    }

    @POST
    @Path("/save")
    @Consumes("application/json")
    public void saveReport(@QueryParam("offerid") Long id, Report report) {
        reportFileService.saveReport(report);
    }

    @GET
    @Path("/delete")
    public String delete(@QueryParam("reportid") Long reportid) {
        Report report = reportEAO.find(Report.class, reportid);

        reportEAO.remove(report);
        return "ok";
    }

    @GET
    @Path("/generate")
    @Produces("application/pdf")
    public File generate(@QueryParam("reportid") Long reportid, @QueryParam("offerid") Long offerId) {

        List<Report> reports = reportFileService.getReports(null, offerId);
        Report foundReport = null;
        for (Report report : reports) {
            if (report.getId().equals(reportid)) {
                foundReport = report;
                break;
            }

        }
        if (foundReport != null) {
            Offer offer = foundReport.getOffer();
            File file = null;//reportFileService.toTempFile(foundReportFile);
            OutputStream os = null;
            try {
                file = File.createTempFile("bbf", "fff");
                os = new FileOutputStream(file);
            } catch (IOException e) {

            }
            String message = reportGenerator.generateReport(os, foundReport, offer);
            if (message == null) {
                Response.ResponseBuilder response = Response.ok(file);
                response.header("Content-Disposition", "attachment; filename=" + foundReport.getFilename() + ".pdf");
                return file;
            } else {
                return null;
            }
            //return response.build();
        } else {
            Response.ResponseBuilder response = Response.status(Response.Status.BAD_REQUEST);
            return null;
        }


    }


//    @POST
//    @Path("/pricelist")
//    @Consumes("multipart/form-data")
//    public Response uploadFile(MultipartFormDataInput input) {
//
//        String fileName = "";
//
//        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
//        List<InputPart> inputParts = uploadForm.get("uploadedFile");
//
//        for (InputPart inputPart : inputParts) {
//
//            try {
//
//                MultivaluedMap<String, String> header = inputPart.getHeaders();
//
//
//                //convert the uploaded file to inputstream
//                InputStream inputStream = inputPart.getBody(InputStream.class, null);
//
//                System.out.println(header);
//
//
//                System.out.println("Done");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        return Response.status(200)
//                .entity("uploadFile is called, Uploaded file name : " + fileName).build();
//
//    }
}

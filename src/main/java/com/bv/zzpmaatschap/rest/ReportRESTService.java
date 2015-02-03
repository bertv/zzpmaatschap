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
        Report foundReport = findReportWithId(reportid, reports);
        if (foundReport != null) {
            Offer offer = foundReport.getOffer();
            File file = null;
            OutputStream os = null;
            try {
                file = File.createTempFile("bbf", "fff");
                os = new FileOutputStream(file);
            } catch (IOException e) {
                saveError(foundReport, e);
                reportEAO.merge(foundReport);
                return null;
            }
            String message = reportGenerator.generateReport(os, foundReport, offer);
            if (message == null) {
                Response.ResponseBuilder response = Response.ok(file);
                response.header("Content-Disposition", "attachment; filename=" + foundReport.getFilename() + ".pdf");
                saveError(foundReport, "Succesvol gegeneerd");
            }else{
                saveError(foundReport,message);
            }

            reportEAO.merge(foundReport);
            return file;

        }
        saveError(foundReport, "Report niet gevonden");
        reportEAO.merge(foundReport);
        return null;

    }

    private void saveError(Report foundReport, String s) {
        foundReport.setStatus(s);
    }

    public Report findReportWithId(Long reportid, List<Report> reports) {
        for (Report report : reports) {
            if (report.getId().equals(reportid)) {
                return report;

            }

        }
        return null;
    }

    private void saveError(Report report, Exception e) {
        saveError(report, e.getMessage());
    }

}

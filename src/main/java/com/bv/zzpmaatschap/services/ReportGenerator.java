package com.bv.zzpmaatschap.services;

import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.report.Report;
import com.bv.zzpmaatschap.model.report.ReportParameter;
import net.sf.jasperreports.engine.*;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Stateless
public class ReportGenerator {

    @Resource(mappedName = "jdbc/ExampleDS")
    private DataSource datasource;

    public String generateReport(OutputStream outputStream, Report reportfile, Offer offer) {

        String message = "Onbekende fout";
        if (offer.getId() != null) {
            try {
                Map<String, Object> jasperParameter = new HashMap<String, Object>();
                for (ReportParameter parameter : reportfile.getParameters()) {
                    jasperParameter.put(parameter.getName(), parameter.getValue());

                }
                jasperParameter.put("p_offer_id", offer.getId().intValue());
                jasperParameter.put(JRParameter.REPORT_LOCALE, new Locale("nl", "NL"));
                JasperPrint jasperPrint;
                if (reportfile.getTemplateReport() != null) {

                    JasperReport jasperReport = JasperCompileManager
                            .compileReport(new ByteArrayInputStream(reportfile.getTemplateReport()));
                    jasperPrint = JasperFillManager
                            .fillReport(jasperReport,
                                    jasperParameter, datasource.getConnection());


                } else {
                    jasperPrint = JasperFillManager
                            .fillReport(new ByteArrayInputStream(reportfile.getCompiledReport()),
                                    jasperParameter, datasource.getConnection());


                }
                JasperExportManager.exportReportToPdfStream(jasperPrint,
                        outputStream);
            } catch (JRException e) {

                message = e.getMessage();
                e.printStackTrace();

            } catch (SQLException e) {

                message = e.getMessage();
                e.printStackTrace();
            } catch (Exception e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        } else {
            message = "Geen offerte geselecteerd.";
        }

        return message;
    }
}

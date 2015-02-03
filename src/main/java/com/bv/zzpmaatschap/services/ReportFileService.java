package com.bv.zzpmaatschap.services;

import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.eao.inferface.IReportEAO;
import com.bv.zzpmaatschap.eao.inferface.IReportParameterEAO;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.report.Report;
import com.bv.zzpmaatschap.model.report.ReportParameter;
import com.bv.zzpmaatschap.model.report.ReportStatus;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

@Singleton
public class ReportFileService implements Serializable {
    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;

    @EJB(beanName = "reportEAO", beanInterface = IReportEAO.class)
    IReportEAO reportEAO;

    @EJB(beanName = "reportParameterEAO", beanInterface = IReportParameterEAO.class)
    IReportParameterEAO reportParameterEAO;
    /**
     *
     */
    private static final long serialVersionUID = -8656353355564616003L;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void extractParametersFromTemplate(String filename, InputStream inputStream, Report report) {

        try {
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            List list = jasperDesign.getParametersList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {

                    System.out.println("name of parameter:" + ((JRParameter) list.get(i)).getName());

                    String name = ((JRParameter) list.get(i)).getName();
                    if (name.startsWith("p_") && !name.startsWith("p_offer_id")) {
                        String value = "";
                        if (((JRParameter) list.get(i)).getDefaultValueExpression() != null && ((JRParameter) list.get(i)).getDefaultValueExpression().getChunks() != null) {
                            JRExpressionChunk[] chunks = ((JRParameter) list.get(i)).getDefaultValueExpression().getChunks();
                            if (chunks.length > 0) {
                                value = ((JRParameter) list.get(i)).getDefaultValueExpression().getChunks()[0].getText();
                            }
                        } else {
                            value = ((JRParameter) list.get(i)).getDescription();
                        }
                        ReportParameter reportParameter = new ReportParameter();
                        reportParameter.setReport(report);
                        reportParameter.setName(name);
                        reportParameter.setValue(value);
                        reportParameterEAO.persist(reportParameter);

                    }

                }
                reportEAO.merge(report);
            }
        } catch (JRException e) {
            report.setStatus(e.getMessage());
            reportEAO.merge(report);
            e.printStackTrace();
        }

    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Report registerReportFile(String filename, InputStream inputStream, Offer offer, boolean isTemplate) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        Report report = new Report();
        if (isTemplate) {
            report.setTemplateReport(buffer.toByteArray());
        } else {
            report.setCompiledReport(buffer.toByteArray());
        }
        report.setOffer(offer);
        report.setStatus("succesvol");
        report.setFilename(filename);
        offer.getReports().add(report);
        reportEAO.persist(report);
        offerEAO.merge(offer);
        return report;
    }

    public List<Report> getReports(Offer offer, Long offerId) {
        return reportEAO.getReports(offer, offerId);
    }

    public void saveReport(Report report) {
        for (ReportParameter parameter : report.getParameters()) {
            parameter.setReport(report);
            if (parameter.getId() == null) {
                reportParameterEAO.persist(parameter);
            } else {
                reportParameterEAO.merge(parameter);
            }
        }
    }
}

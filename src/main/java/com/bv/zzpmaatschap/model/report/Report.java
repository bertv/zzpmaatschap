package com.bv.zzpmaatschap.model.report;

import com.bv.zzpmaatschap.model.Offer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@XmlRootElement
@Table(name="report")
@NamedQueries(value = {@NamedQuery(name = "selectAllReports", query = "select r from Report r"), @NamedQuery(name = "selectReports", query = "select r from Report r where r.offer.id=:offer_id")})
public class Report implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 163505635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String filename;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @XmlTransient
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] compiledReport;
    @XmlTransient
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] templateReport;

    //@JsonIgnore
    //@XmlTransient
    @OneToMany(mappedBy = "report", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<ReportParameter> parameters = new HashSet<ReportParameter>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    @XmlTransient
    @JsonIgnore
    public byte[] getCompiledReport() {
        return compiledReport;
    }

    public void setCompiledReport(byte[] compiledReport) {
        this.compiledReport = compiledReport;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Offer offer;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
    @XmlTransient
    @JsonIgnore
    public byte[] getTemplateReport() {
        return templateReport;
    }

    public void setTemplateReport(byte[] templateReport) {
        this.templateReport = templateReport;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Set<ReportParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<ReportParameter> parameters) {
        this.parameters = parameters;
    }
}

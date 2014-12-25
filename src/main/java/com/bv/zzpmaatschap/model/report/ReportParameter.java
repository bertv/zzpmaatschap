package com.bv.zzpmaatschap.model.report;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;


@Entity
@XmlRootElement
@Table(name="reportparameter")
@NamedQueries(value = {@NamedQuery(name = "selectAllReportParameters", query = "select r from ReportParameter r"), @NamedQuery(name = "selectReportParameters", query = "select r from ReportParameter r where r.report.id=:report_id")})
public class ReportParameter implements Serializable {
    private static final long serialVersionUID = 197635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @XmlTransient
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Report report;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @XmlTransient
    @JsonIgnore
    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

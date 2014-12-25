package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.model.Company;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CCompanyCheck {


    private String name;
    private boolean check;



    private Long id;

    public CCompanyCheck() {

    }

    public CCompanyCheck(Company company, boolean b) {
        this.name = company.getName();
        this.check = b;
        this.id = company.getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return check;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}

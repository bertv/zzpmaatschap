package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.model.User;

/**
 * Created by bertv on 2-8-14.
 */
public class CAccount extends User {

    public CAccount() {

    }

    private CCompanyCheck[] companyCheck;
    private CRole crole;

    public CCompanyCheck[] getCompanyCheck() {
        return this.companyCheck;
    }

    public void setCompanyCheck(CCompanyCheck[] companyCheck) {
        this.companyCheck = companyCheck;
    }

    public CAccount(User user, int i,CRole roleCheckList) {
        this.companyCheck = new CCompanyCheck[i];
        this.crole=roleCheckList;
        this.setPassword(user.getPassword());
        this.setRole(user.getRole());
        this.setAddress(user.getAddress());
        this.setFirstname(user.getFirstname());
        this.setSurname(user.getSurname());
        this.setEmail(user.getEmail());
        this.setId(user.getId());
        this.setCreationDate(user.getCreationDate());
        this.setUsername(user.getUsername());
    }

    public CRole getCRole() {
        return crole;
    }

    public void setCRole(CRole roleCheck) {
        this.crole = roleCheck;
    }
}

package com.bv.zzpmaatschap.rest;


public class CRoleCheck {
    private boolean check;
    private CRole role;
    public CRoleCheck(){

    }
    public CRoleCheck(CRole role,boolean check){
        this.check=check;
        this.role=role;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public CRole getRole() {
        return role;
    }

    public void setRole(CRole role) {
        this.role = role;
    }
}

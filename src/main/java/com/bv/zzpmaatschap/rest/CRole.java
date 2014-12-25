package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.model.Role;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CRole extends Role {

    public CRole(){

    }
    public CRole(Role role){
        this.setName(role.getName());
    }
    public CRole(String role){
        this.setName(role);

    }
}

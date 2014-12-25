package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.Offer;

import java.util.List;


public class OfferTenant {
    private Long id;

    private String name;

    private Company tenantId1;

    private Company tenantId2;

    private Company tenantId3;
    private Company tenantId4;
    public OfferTenant(){

    }
    public OfferTenant(Offer offer, List<Company> allCompanies) {
        this.id = offer.getId();
        this.name = offer.getName();
        for (Company comp : allCompanies) {
            if (offer.getTenantId1().longValue() == comp.getId()) {
                this.tenantId1 = comp;
            }
            if ((offer.getTenantId2()==null?null:offer.getTenantId2().longValue()) == comp.getId()) {
                this.tenantId2 = comp;
            }
            if ((offer.getTenantId3()==null?null:offer.getTenantId3().longValue()) == comp.getId()) {
                this.tenantId3 = comp;
            }
            if ((offer.getTenantId4()==null?null:offer.getTenantId4().longValue()) == comp.getId()) {
                this.tenantId4 = comp;
            }
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Company getTenantId1() {
        return tenantId1;
    }

    public void setTenantId1(Company tenantId1) {
        this.tenantId1 = tenantId1;
    }

    public Company getTenantId2() {
        return tenantId2;
    }

    public void setTenantId2(Company tenantId2) {
        this.tenantId2 = tenantId2;
    }

    public Company getTenantId3() {
        return tenantId3;
    }

    public void setTenantId3(Company tenantId3) {
        this.tenantId3 = tenantId3;
    }

    public Company getTenantId4() {
        return tenantId4;
    }

    public void setTenantId4(Company tenantId4) {
        this.tenantId4 = tenantId4;
    }
}

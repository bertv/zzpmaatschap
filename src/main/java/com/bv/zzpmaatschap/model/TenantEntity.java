package com.bv.zzpmaatschap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Offer.class})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId1", type = "integer"), @ParamDef(name = "tenantId2", type = "integer"), @ParamDef(name = "tenantId3", type = "integer"), @ParamDef(name = "tenantId4", type = "integer")})
@Filters(@Filter(name = "tenantFilter", condition = "tenant_id1 in (:tenantId1,:tenantId2,:tenantId3,:tenantId4) or tenant_id2 in (:tenantId1,:tenantId2,:tenantId3,:tenantId4) or tenant_id3 in (:tenantId1,:tenantId2,:tenantId3,:tenantId4) or tenant_id4 in (:tenantId1,:tenantId2,:tenantId3,:tenantId4)"))
public class TenantEntity {

    @JsonIgnore
    //@XmlTransient
    @Column(name = "tenant_id1", nullable = false, updatable = true)
    private Integer tenantId1;

    @JsonIgnore
    //@XmlTransient
    @Column(name = "tenant_id2", nullable = true, updatable = true)
    private Integer tenantId2;

    @JsonIgnore
    //@XmlTransient
    @Column(name = "tenant_id3", nullable = true, updatable = true)
    private Integer tenantId3;

    @JsonIgnore
    //@XmlTransient
    @Column(name = "tenant_id4", nullable = true, updatable = true)
    private Integer tenantId4;

    @JsonIgnore
    //@XmlTransient
    public Integer getTenantId1() {
        return tenantId1;
    }

    public void setTenantId1(Integer tenantId1) {
        this.tenantId1 = tenantId1;
    }

    public void copyTenant(TenantEntity tenantEntity) {
        tenantEntity.setTenantId1(this.tenantId1);
        tenantEntity.setTenantId2(this.tenantId2);
        tenantEntity.setTenantId3(this.tenantId3);
        tenantEntity.setTenantId4(this.tenantId4);
    }

    @JsonIgnore
    public Integer getTenantId2() {
        return tenantId2;
    }

    @JsonIgnore
    public Integer getTenantId3() {
        return tenantId3;
    }

    @JsonIgnore
    public Integer getTenantId4() {
        return tenantId4;
    }

    public void setTenantId2(Integer tenantId2) {
        this.tenantId2 = tenantId2;
    }

    public void setTenantId4(Integer tenantId4) {
        this.tenantId4 = tenantId4;
    }

    public void setTenantId3(Integer tenantId3) {
        this.tenantId3 = tenantId3;
    }
}

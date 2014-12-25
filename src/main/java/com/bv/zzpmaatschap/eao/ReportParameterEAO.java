package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IReportEAO;
import com.bv.zzpmaatschap.eao.inferface.IReportParameterEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.report.Report;
import com.bv.zzpmaatschap.model.report.ReportParameter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "reportParameterEAO")
public class ReportParameterEAO implements IReportParameterEAO {

    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;


    public void persist(ReportParameter obj) {
        em.persist(obj);

    }

    public ReportParameter merge(ReportParameter obj) {
        return em.merge(obj);
    }

    public ReportParameter find(Class<ReportParameter> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    public void refresh(ReportParameter entity) {
        em.refresh(entity);

    }

    @Override
    public void remove(ReportParameter item) {

        em.remove(item);

    }


}
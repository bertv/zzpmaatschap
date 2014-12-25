package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IReportEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.report.Report;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "reportEAO")
public class ReportEAO implements IReportEAO {

    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;


    public void persist(Report obj) {
        em.persist(obj);

    }

    public Report merge(Report obj) {
        return em.merge(obj);
    }

    public Report find(Class<Report> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    public void refresh(Report entity) {
        em.refresh(entity);

    }

    @Override
    public void remove(Report item) {
        item.getOffer().setReports(null);

        em.remove(item);
    }


    @Override
    public List<Report> getReports(Offer offer, Long offerId) {

        TypedQuery<Report> queryBuilder = em.createNamedQuery(
                "selectReports", Report.class);
        if (offerId != null) {
            queryBuilder.setParameter("offer_id", offerId);
        }
        if (offer != null) {
            queryBuilder.setParameter("offer_id", offer.getId());
        }
        return queryBuilder.getResultList();
    }
}
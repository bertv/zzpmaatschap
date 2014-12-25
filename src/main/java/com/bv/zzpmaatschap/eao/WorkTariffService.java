package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IWorkTariffEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.WorkTariff;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless(name = "workTariffEAO")
public class WorkTariffService implements IWorkTariffEAO {

    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;


    public void persist(WorkTariff obj) {
        em.persist(obj);

    }

    public WorkTariff merge(WorkTariff obj) {
        return em.merge(obj);
    }

    public WorkTariff find(Class<WorkTariff> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    public void refresh(WorkTariff entity) {
        em.refresh(entity);

    }

    @Override
    public void remove(WorkTariff entity) {
        em.remove(entity);

    }


    @Override
    public List<WorkTariff> getAllTariffs() {
        TypedQuery<WorkTariff> criteria = em.createNamedQuery("getAllTariffs", WorkTariff.class);

        return criteria.getResultList();
    }
}
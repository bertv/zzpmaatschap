package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapperImpl;
import com.bv.zzpmaatschap.model.Offer;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Stateless(name = "offerEAO")
public class OfferEAO implements IOfferEAO, Serializable {
    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    private EntityManager em;


    public void persist(Offer obj) {
        em.persist(obj);

    }


    public Offer merge(Offer obj) {
        return em.merge(obj);
    }


    public Offer find(Class<Offer> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }


    public void refresh(Offer entity) {
        em.refresh(entity);

    }


    public void remove(Offer entity) {
        em.remove(entity);
        em.flush();
    }


    public List<Offer> getAllOffers() {
        TypedQuery<Offer> criteria = em.createNamedQuery("selectAll",
                Offer.class);


        return criteria.getResultList();
    }

    @RolesAllowed("admin")
    @Override
    public List<Offer> getAllOffersNoRestrictions() {
        em.setProperty(MultiTenantEntityManagerWrapperImpl.SKIP_FILTER, true);
        List<Offer> offers= getAllOffers();
        em.setProperty(MultiTenantEntityManagerWrapperImpl.SKIP_FILTER, false);
        return offers;
    }


    public List<Offer> getUserOffers(Long userId) {
        Query criteria = em.createNamedQuery("getUserOffers");
        criteria.setParameter("user_id", userId);

        return criteria.getResultList();
    }
}
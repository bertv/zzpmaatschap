package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapperImpl;
import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.Offer;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless(name = "offerEAO")
public class OfferEAO implements IOfferEAO, Serializable {
    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    private EntityManager em;
    private double popularity = 0;
    private Double gemiddeldePopulariteit = null;


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
        List<Offer> offers = getAllOffers();
        em.setProperty(MultiTenantEntityManagerWrapperImpl.SKIP_FILTER, false);
        return offers;
    }


    public List<Offer> getUserOffers(Long userId) {
        Query criteria = em.createNamedQuery("getUserOffers");
        criteria.setParameter("user_id", userId);

        return criteria.getResultList();
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void removeOldOffers(Date olddate, IItemService itemService) {
        try {
            if (gemiddeldePopulariteit == null) {
                bepaalGemiddeldePopulariteit(itemService);
            }
            for (Offer offer : this.getAllOffersNoRestrictions()) {
                if (offer.getCreationDate() != null && offer.getCreationDate().before(olddate)) {
                    for (Item item : offer.getItems()) {
                        item.setOffer(null);
                        if (!((double) item.getPopularity() * 0.9 >= gemiddeldePopulariteit)) {
                            itemService.remove(item);
                        } else {
                            itemService.merge(item);
                        }
                    }

                    this.remove(offer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bepaalGemiddeldePopulariteit(IItemService itemService) {
        int size = 0;
        for (Item item : itemService.getAllItems()) {
            popularity = popularity + item.getPopularity();
            if (item.getPopularity() > 0) {
                size++;
            }
        }
        gemiddeldePopulariteit = popularity / size;

    }
}
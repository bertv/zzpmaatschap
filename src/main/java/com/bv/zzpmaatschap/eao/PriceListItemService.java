package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IPriceListItemEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.pricelist.PriceListItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless(name = "priceListItemEAO")
public class PriceListItemService implements IPriceListItemEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED
    )

	public void persist(PriceListItem obj) {

        em.persist(obj);

        em.flush();
	}

	public PriceListItem merge(PriceListItem obj) {
		return em.merge(obj);
	}

	public PriceListItem find(Class<PriceListItem> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(PriceListItem entity) {
		em.refresh(entity);

	}




	public void remove(PriceListItem entity) {
		em.remove(entity);
		
	}

    @Override
    public List<PriceListItem> getPriceListItemsByName(String name) {
        TypedQuery<PriceListItem> queryBuilder = em.createNamedQuery(
                "selectPriceListItem", PriceListItem.class);
        queryBuilder.setParameter("name", ""+name.replaceAll("<","").replaceAll(";","").replaceAll(">","").replaceAll("\\(","").replaceAll("\\)","")+"").setMaxResults(50);
        return queryBuilder.getResultList();
    }

    @Override
    public long getPriceListItemCount(long id) {
        Query queryBuilder = em.createNamedQuery(
                "getPriceList");
        queryBuilder.setParameter("priceListId",id);
        Number result = (Number) queryBuilder.getSingleResult();
        return result.longValue();
    }
}
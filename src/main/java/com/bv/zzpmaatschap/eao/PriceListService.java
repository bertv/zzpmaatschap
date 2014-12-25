package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IPriceListEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.pricelist.PriceList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless(name = "priceListEAO")
public class PriceListService implements IPriceListEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	public List<PriceList> getAllPriceLists() {
		TypedQuery<PriceList> queryBuilder = em.createNamedQuery(
				"selectPriceLists", PriceList.class);

		return queryBuilder.getResultList();
	}

    @Override
    public void remove(Long id) {
       em.remove( em.find(PriceList.class,id));
    }



    public void persist(PriceList obj) {

		em.persist(obj);
	}
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PriceList merge(PriceList obj) {
		PriceList priceList= em.merge(obj);
        em.flush();
        return priceList;
	}

	public PriceList find(Class<PriceList> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(PriceList entity) {
		em.refresh(entity);

	}




	public void remove(PriceList entity) {
		em.remove(entity);
		
	}

}
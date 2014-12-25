package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.Offer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Stateless(name = "itemService")
public class ItemService implements IItemService {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	public List<Item> getChangedItems(Offer offer) {

		TypedQuery<Item> criteria = em.createNamedQuery("selectItem",
				Item.class);

		criteria.setParameter("offer", offer.getId());

		return criteria.getResultList();

	}

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(Item item) {

        item.setCreated(new Date());
		em.persist(item);

		item.setOrderNumber((item.getId().intValue()));
		merge(item);
	}

	public Item merge(Item obj) {
		
		return em.merge(obj);

	}

	public Item find(Class<Item> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(Item entity) {
		em.refresh(entity);

	}

	@Override
	public List<Item> getOrderedItems(Offer offer) {
        TypedQuery<Item> criteria=null;
        if (offer.getId()!=null){
            criteria = em.createNamedQuery("getOrderedItems",Item.class);
            criteria.setParameter("offer", offer.getId());
        }else{
            return new ArrayList<Item>();
        }
		return criteria.getResultList();
	}

	@Override
	public void remove(Item item) {
		em.remove(item);
		
	}

	@Override
	public List<Item> getDescriptions(Offer offer,String query) {
		TypedQuery<Item> criteria = em.createNamedQuery("getDescriptions",Item.class);
		criteria.setParameter("offer", offer.getId());
		criteria.setParameter("query", query);
		return criteria.getResultList();
	}

	@Override
	public Item getItemByDescription(Offer offer,String description) {
		TypedQuery<Item> criteria = em.createNamedQuery("getItemByDescription",Item.class);
		criteria.setParameter("offer", offer.getId());
		criteria.setParameter("description", description);
		if (criteria.getResultList().size()>0){
		return criteria.getResultList().get(0);
		}
		return null;
	}

    @Override
    public List<Item> getAllItems() {
        TypedQuery<Item> criteria = em.createNamedQuery("getAllItems",Item.class);
        return criteria.getResultList();
    }
}

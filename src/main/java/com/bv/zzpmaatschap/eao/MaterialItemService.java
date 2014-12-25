package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IMaterialItemEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.MaterialItem;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
@Stateless(name = "materialItemEAO")
public class MaterialItemService implements IMaterialItemEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	
	public void persist(MaterialItem obj) {
		em.persist(obj);

	}

	public MaterialItem merge(MaterialItem obj) {
		return em.merge(obj);
		
	}

	public MaterialItem find(Class<MaterialItem> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(MaterialItem entity) {
		em.refresh(entity);

	}

	@Override
	public void remove(MaterialItem item) {
		em.remove(item);
		
	}

	

	

}
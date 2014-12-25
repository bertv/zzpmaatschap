package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IWorkItemEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.WorkItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
@Stateless(name = "workItemEAO")
public class WorkItemService implements IWorkItemEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	
	public void persist(WorkItem obj) {
		em.persist(obj);

	}

	public WorkItem merge(WorkItem obj) {
		return em.merge(obj);
	}

	public WorkItem find(Class<WorkItem> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(WorkItem entity) {
		em.refresh(entity);

	}

	@Override
	public void remove(WorkItem item) {
		
		em.remove(item);
		
	}

	

	

}
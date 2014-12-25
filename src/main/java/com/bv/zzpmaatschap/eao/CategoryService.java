package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.ICategoryEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Category;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless(name = "categoryEAO")
public class CategoryService implements ICategoryEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	public List<Category> getAllCategories() {
		TypedQuery<Category> queryBuilder = em.createNamedQuery(
				"selectCategories", Category.class);

		return queryBuilder.getResultList();
	}

	public void persist(Category obj) {
		em.persist(obj);

	}

	public Category merge(Category obj) {
		return em.merge(obj);
	}

	public Category find(Class<Category> entityClass, Object primaryKey) {
		return em.find(entityClass, primaryKey);
	}

	public void refresh(Category entity) {
		em.refresh(entity);

	}

	@Override
	public Category findCategory(String categoryName) {
		TypedQuery<Category> queryBuilder = em.createNamedQuery(
				"findCategory", Category.class);
		queryBuilder.setParameter("description", categoryName);
		if (queryBuilder.getResultList().size()>0){
		return queryBuilder.getResultList().get(0);}
		return null;
	}

	@Override
	public void remove(Category entity) {
		em.remove(entity);
		
	}

}
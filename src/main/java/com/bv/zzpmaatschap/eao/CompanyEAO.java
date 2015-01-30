package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Category;
import com.bv.zzpmaatschap.model.Company;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless(name = "companyEAO")
public class CompanyEAO implements ICompanyEAO {

	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;


    @Override
    public List<Company> getAllCompanies() {
        HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
        Session hses = hem.getSession();
        Query criteria = hses.createQuery("select c from Company c");
        criteria.setCacheable(true);

        return criteria.list();
    }

    @Override
    public List<Company> getCompanies(Long offerId) {
        javax.persistence.Query queryBuilder = em.createNamedQuery(
                "getCompaniesForOffer");
        queryBuilder.setParameter("id", offerId);
        return queryBuilder.getResultList();
    }

    @Override
    public boolean hasUsers(Company company) {
        javax.persistence.Query queryBuilder = em.createNamedQuery(
                "hasUsers");
        queryBuilder.setParameter("id", company.getId());
        return (Long)queryBuilder.getSingleResult()>0;
    }

    @Override
    public void persist(Company obj) {
        em.persist(obj);
    }

    @Override
    public Company merge(Company obj) {
        return em.merge(obj);
    }

    @Override
    public Company find(Class<Company> entityClass, Object primaryKey) {
        return em.find(entityClass,primaryKey);
    }

    @Override
    public void refresh(Company entity) {
        em.refresh(entity);
    }

    @Override
    public void remove(Company entity) {
        em.remove(entity);
    }
}

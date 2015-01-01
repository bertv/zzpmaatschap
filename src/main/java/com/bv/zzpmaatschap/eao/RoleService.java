package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IRoleEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Role;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Stateless(name = "roleEAO")
public class RoleService implements IRoleEAO {

    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;


    public void persist(Role obj) {
        em.persist(obj);

    }

    public Role merge(Role obj) {
        return em.merge(obj);
    }

    public Role find(Class<Role> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    public void refresh(Role entity) {
        em.refresh(entity);

    }

    @Override
    public void remove(Role entity) {
        em.remove(entity);
    }


    @Override
    public List<String> getRoles() {
        Query queryBuilder = em.createNamedQuery(
                "getAllRoles");
        return queryBuilder.getResultList();
    }

}
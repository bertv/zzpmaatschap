package com.bv.zzpmaatschap.eao;

import com.bv.zzpmaatschap.eao.inferface.IUserEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless(name = "userEAO")
public class UserEAO implements IUserEAO {
    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;

    @Override
    public void persist(User obj) {
        em.persist(obj);
    }

    @Override
    public User merge(User obj) {
        return em.merge(obj);
    }

    @Override
    public User find(Class<User> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    @Override
    public void refresh(User entity) {
        em.refresh(entity);
    }

    @Override
    public void remove(User entity) {
        em.remove(entity);
    }
}

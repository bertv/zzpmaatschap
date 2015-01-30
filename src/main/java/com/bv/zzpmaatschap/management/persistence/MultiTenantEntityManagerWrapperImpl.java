package com.bv.zzpmaatschap.management.persistence;

import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.TenantEntity;
import com.bv.zzpmaatschap.model.User;
import com.bv.zzpmaatschap.services.UserService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.security.Principal;
import java.util.*;

@Stateless(name = "mtem")
public class MultiTenantEntityManagerWrapperImpl implements
        MultiTenantEntityManagerWrapper {

    public static final String SKIP_FILTER = "skipFilter";

    @PersistenceContext//(type=PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Resource
    SessionContext context;

    @EJB
    private UserService userService;

    @EJB(beanName = "companyEAO", beanInterface = ICompanyEAO.class)
    ICompanyEAO companyEAO;

    private boolean skipFilter;

    private List<Integer> getTenantId(Session hses, List<User> users) {
        HibernateEntityManager hem = null;

        if (hses == null) {
            hem = em.unwrap(HibernateEntityManager.class);
            hses = hem.getSession();
        }
        hses.disableFilter("tenantFilter");
        if (users == null) {
            users = getUsers(hses);
        }


        Principal p = context.getCallerPrincipal();
        List<Integer> tenantIds = new ArrayList<>();
        User user = getCurrentUser(users, p.getName());
        if (user != null) {
            extractAllCompanies(user.getCompanies(), tenantIds);
            if (tenantIds.isEmpty()) {
                if (user.getDefaultCompany() != null) {
                    tenantIds.add(user.getDefaultCompany().getId().intValue());
                }
            }
        }
        Collections.sort(tenantIds);
        return tenantIds;
    }

    private List<User> getUsers(Session hses) {
        List<User> users;
        org.hibernate.Query criteria = hses.createQuery("select u from User u");
        criteria.setCacheable(true);
        criteria.setCacheRegion("users");
        users = criteria.list();
        return users;
    }

    private void extractAllCompanies(Collection<Company> companies, List<Integer> tenantIds) {
        for (Company company : companies) {
            tenantIds.add(company.getId().intValue());
        }
    }

    private List<User> getUsers(){
        HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
        Session hses = hem.getSession();
        List<User> users = getUsers(hses);
        return users;
    }

    private EntityManager getMultiTenantEntityManager() {
        HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
        Session hses = hem.getSession();
        List<User> users = getUsers(hses);
        if (!skipFilter && !getCurrentUserRole(users).equalsIgnoreCase("admin")) {
            if (hses.getEnabledFilter("tenantFilter") == null) {
                List<Integer> tenantIds = getTenantId(hses, users);

                Filter filter = hses.enableFilter("tenantFilter");

                if (tenantIds.size() == 1) {
                    filter.setParameter("tenantId" + (1), tenantIds.get(0));
                    filter.setParameter("tenantId" + (2), tenantIds.get(0));
                    filter.setParameter("tenantId" + (3), tenantIds.get(0));
                    filter.setParameter("tenantId" + (4), tenantIds.get(0));
                } else if (tenantIds.size() == 2) {

                    filter.setParameter("tenantId" + (1), tenantIds.get(0));
                    filter.setParameter("tenantId" + (2), tenantIds.get(1));
                    filter.setParameter("tenantId" + (3), tenantIds.get(0));
                    filter.setParameter("tenantId" + (4), tenantIds.get(0));
                } else if (tenantIds.size() == 3) {

                    filter.setParameter("tenantId" + (1), tenantIds.get(0));
                    filter.setParameter("tenantId" + (2), tenantIds.get(1));
                    filter.setParameter("tenantId" + (3), tenantIds.get(2));
                    filter.setParameter("tenantId" + (4), tenantIds.get(0));
                } else if (tenantIds.size() >= 4) {

                    filter.setParameter("tenantId" + (1), tenantIds.get(0));
                    filter.setParameter("tenantId" + (2), tenantIds.get(1));
                    filter.setParameter("tenantId" + (3), tenantIds.get(2));
                    filter.setParameter("tenantId" + (4), tenantIds.get(3));
                }
                else {

                    filter.setParameter("tenantId" + (1), 0);
                    filter.setParameter("tenantId" + (2), 0);
                    filter.setParameter("tenantId" + (3), 0);
                    filter.setParameter("tenantId" + (4), 0);
                }
            }
        } else {
            hses.disableFilter("tenantFilter");
        }
        if (em == null) {
            throw new RuntimeException("Tenant unknown");
        }
        return em;
    }

    private String getCurrentUserRole(List<User> users) {
        User currentUser = getCurrentUser(users, context.getCallerPrincipal().getName());
        if (currentUser == null) {
            return "ANONYMOUS";
        }
        return currentUser.getRole().getName();
    }

    private User getCurrentUser(List<User> users, String username) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }

        }
        return null;
    }

    private void tenancify(Object entity) {

        if (entity != null && !skipFilter && !getCurrentUserRole(getUsers()).equalsIgnoreCase("admin")) {
            if (entity instanceof TenantEntity) {
                List<Integer> tenantIds = getTenantId(null, null);
                if (tenantIds.size() > 0) {
                     if (tenantIds.size()==1){
                        ((TenantEntity) entity).setTenantId1(((TenantEntity) entity).getTenantId1() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId1());
                        ((TenantEntity) entity).setTenantId2(((TenantEntity) entity).getTenantId2() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId2());
                        ((TenantEntity) entity).setTenantId3(((TenantEntity) entity).getTenantId3() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId3());
                        ((TenantEntity) entity).setTenantId4(((TenantEntity) entity).getTenantId4() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId4());

                    }else if(tenantIds.size()==2){
                         ((TenantEntity) entity).setTenantId1(((TenantEntity) entity).getTenantId1() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId1());
                         ((TenantEntity) entity).setTenantId2(((TenantEntity) entity).getTenantId2() == null ? tenantIds.get(1) : ((TenantEntity) entity).getTenantId2());
                         ((TenantEntity) entity).setTenantId3(((TenantEntity) entity).getTenantId3() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId3());
                         ((TenantEntity) entity).setTenantId4(((TenantEntity) entity).getTenantId4() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId4());

                     }else if(tenantIds.size()==3){
                         ((TenantEntity) entity).setTenantId1(((TenantEntity) entity).getTenantId1() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId1());
                         ((TenantEntity) entity).setTenantId2(((TenantEntity) entity).getTenantId2() == null ? tenantIds.get(1) : ((TenantEntity) entity).getTenantId2());
                         ((TenantEntity) entity).setTenantId3(((TenantEntity) entity).getTenantId3() == null ? tenantIds.get(2) : ((TenantEntity) entity).getTenantId3());
                         ((TenantEntity) entity).setTenantId4(((TenantEntity) entity).getTenantId4() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId4());

                     }else if(tenantIds.size()>=4){
                         ((TenantEntity) entity).setTenantId1(((TenantEntity) entity).getTenantId1() == null ? tenantIds.get(0) : ((TenantEntity) entity).getTenantId1());
                         ((TenantEntity) entity).setTenantId2(((TenantEntity) entity).getTenantId2() == null ? tenantIds.get(1) : ((TenantEntity) entity).getTenantId2());
                         ((TenantEntity) entity).setTenantId3(((TenantEntity) entity).getTenantId3() == null ? tenantIds.get(2) : ((TenantEntity) entity).getTenantId3());
                         ((TenantEntity) entity).setTenantId4(((TenantEntity) entity).getTenantId4() == null ? tenantIds.get(3) : ((TenantEntity) entity).getTenantId4());

                     }
                } else {
                    ((TenantEntity) entity).setTenantId1(((TenantEntity) entity).getTenantId1() == null ? 0 : ((TenantEntity) entity).getTenantId1());
                    ((TenantEntity) entity).setTenantId2(((TenantEntity) entity).getTenantId2() == null ? 0 : ((TenantEntity) entity).getTenantId2());
                    ((TenantEntity) entity).setTenantId3(((TenantEntity) entity).getTenantId3() == null ? 0 : ((TenantEntity) entity).getTenantId3());
                    ((TenantEntity) entity).setTenantId4(((TenantEntity) entity).getTenantId4() == null ? 0 : ((TenantEntity) entity).getTenantId4());
                }
                if (((TenantEntity) entity).getTenantId1() == null) {
                    ((TenantEntity) entity).setTenantId1(tenantIds.get(0));
                }
                if (((TenantEntity) entity).getTenantId2() == null) {
                    ((TenantEntity) entity).setTenantId2(tenantIds.get(0));
                }
                if (((TenantEntity) entity).getTenantId3() == null) {
                    ((TenantEntity) entity).setTenantId3(tenantIds.get(0));
                }
                if (((TenantEntity) entity).getTenantId4() == null) {
                    ((TenantEntity) entity).setTenantId4(tenantIds.get(0));
                }
            }
        }
    }

    // The delegates
    @Override
    public void persist(Object entity) {
        tenancify(entity);
        em.persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        tenancify(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(Object entity) {
        tenancify(entity);
        getMultiTenantEntityManager().remove(entity);
    }

    @Override
    public void close() {
        getMultiTenantEntityManager().close();
    }

    @Override
    public boolean contains(Object arg0) {
        tenancify(arg0);
        return getMultiTenantEntityManager().contains(arg0);
    }

    @Override
    public Query createNamedQuery(String arg0) {

        return getMultiTenantEntityManager().createNamedQuery(arg0);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
        return getMultiTenantEntityManager().createNamedQuery(arg0, arg1);
    }

    @Override
    public Query createNativeQuery(String arg0) {
        return getMultiTenantEntityManager().createNativeQuery(arg0);
    }

    @Override
    public Query createNativeQuery(String arg0, Class arg1) {
        return getMultiTenantEntityManager().createNativeQuery(arg0, arg1);
    }

    @Override
    public Query createNativeQuery(String arg0, String arg1) {
        return getMultiTenantEntityManager().createNativeQuery(arg0, arg1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return null;
    }

    @Override
    public Query createQuery(String arg0) {
        return getMultiTenantEntityManager().createQuery(arg0);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
        return getMultiTenantEntityManager().createQuery(arg0);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return null;
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
        return getMultiTenantEntityManager().createQuery(arg0, arg1);
    }

    @Override
    public void detach(Object arg0) {
        tenancify(arg0);
        getMultiTenantEntityManager().detach(arg0);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1) {
        tenancify(arg1);
        return getMultiTenantEntityManager().find(arg0, arg1);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
        return getMultiTenantEntityManager().find(arg0, arg1, arg2);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
        return getMultiTenantEntityManager().find(arg0, arg1, arg2);
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2,
                      Map<String, Object> arg3) {
        return getMultiTenantEntityManager().find(arg0, arg1, arg2, arg3);
    }

    @Override
    public void flush() {
        getMultiTenantEntityManager().flush();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return getMultiTenantEntityManager().getCriteriaBuilder();
    }

    @Override
    public Object getDelegate() {
        return getMultiTenantEntityManager().getDelegate();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {

        return getMultiTenantEntityManager().getEntityManagerFactory();
    }

    @Override
    public FlushModeType getFlushMode() {
        return getMultiTenantEntityManager().getFlushMode();
    }

    @Override
    public LockModeType getLockMode(Object arg0) {
        return getMultiTenantEntityManager().getLockMode(arg0);
    }

    @Override
    public Metamodel getMetamodel() {
        return getMultiTenantEntityManager().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> tClass) {
        return null;
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return null;
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return null;
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> tClass) {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return getMultiTenantEntityManager().getProperties();
    }

    @Override
    public <T> T getReference(Class<T> arg0, Object arg1) {
        tenancify(arg1);
        return getMultiTenantEntityManager().getReference(arg0, arg1);
    }

    @Override
    public EntityTransaction getTransaction() {
        return getMultiTenantEntityManager().getTransaction();
    }

    @Override
    public boolean isOpen() {
        return getMultiTenantEntityManager().isOpen();
    }

    @Override
    public void joinTransaction() {
        getMultiTenantEntityManager().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    @Override
    public void lock(Object arg0, LockModeType arg1) {

        getMultiTenantEntityManager().lock(arg0, arg1);
    }

    @Override
    public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        getMultiTenantEntityManager().lock(arg0, arg1, arg2);
    }

    @Override
    public void refresh(Object arg0) {
        tenancify(arg0);
        getMultiTenantEntityManager().refresh(arg0);
    }

    @Override
    public void refresh(Object arg0, Map<String, Object> arg1) {
        tenancify(arg0);
        getMultiTenantEntityManager().refresh(arg0, arg1);
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1) {
        tenancify(arg0);
        getMultiTenantEntityManager().refresh(arg0, arg1);
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
        getMultiTenantEntityManager().refresh(arg0, arg1, arg2);
    }

    @Override
    public void setFlushMode(FlushModeType arg0) {
        getMultiTenantEntityManager().setFlushMode(arg0);
    }

    @Override
    public void setProperty(String arg0, Object arg1) {
        if (SKIP_FILTER.equalsIgnoreCase(arg0)) {
            skipFilter = (Boolean) arg1;
        } else {
            getMultiTenantEntityManager().setProperty(arg0, arg1);
        }
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        tenancify(arg0);
        return getMultiTenantEntityManager().unwrap(arg0);
    }

    @Override
    public void clear() {
        getMultiTenantEntityManager().clear();

    }
}
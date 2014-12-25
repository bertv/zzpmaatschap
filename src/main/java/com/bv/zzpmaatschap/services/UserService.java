package com.bv.zzpmaatschap.services;

import com.bv.zzpmaatschap.eao.inferface.IRoleEAO;
import com.bv.zzpmaatschap.eao.inferface.IUserEAO;
import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;
import com.bv.zzpmaatschap.model.Role;
import com.bv.zzpmaatschap.model.User;
import com.bv.zzpmaatschap.services.interfaces.IUserService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.security.Principal;
import java.util.List;


@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless(name="userService")
public class UserService  {
    @EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
    // @PersistenceContext
    private EntityManager em;

    @EJB(beanName = "roleEAO", beanInterface = IRoleEAO.class)
    IRoleEAO roleEAO;

    @EJB(beanName = "userEAO", beanInterface = IUserEAO.class)
    IUserEAO userEAO;

//    @EJB
//    private MailService mailService;

    public User getCurrentUser(Principal principal) {


        if (principal != null) {
            String userName = principal.getName();
            em.setProperty("skipFilter", true);
            TypedQuery<User> q = em.createNamedQuery("getUserByName",
                    User.class);
            q.setParameter("name", userName);
            User user = q.getResultList().get(0);
            em.setProperty("skipFilter", false);
            return user;

        }
        return null;

    }

    public User getCurrentUser(String principal) {

        if (principal != null) {
            String userName = principal;
            em.setProperty("skipFilter", true);
            TypedQuery<User> q = em.createQuery("select u from User u where u.username=:name", User.class);
            q.setParameter("name", userName);
            if (q.getResultList().size() > 0) {
                User user = q.getResultList().get(0);
                em.setProperty("skipFilter", false);
                return user;
            }
        }
        return null;

    }

    public void register(User user) {
        user.setPassword("test1234567890");
        Role inactive = findInactiveRole(roleEAO.getRoles());
        inactive.setUsername(user.getUsername());
        user.setRole(inactive);
        roleEAO.persist(inactive);
        em.setProperty("skipFilter", true);
        em.persist(user);

        em.setProperty("skipFilter", false);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("Beste " + user.getFirstname() + " " + user.getSurname());
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("Bedankt voor het registreren van uw gegevens.");
        sb.append("<br/>");
        sb.append("Uw aanvraag wordt z.s.m. in behandeling genomen.");
        sb.append("U ontvangt een email wanneer uw account geactiveerd wordt.");
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("Met vriendelijke groet,<br/>");
        sb.append("zzpmaatschap team<br/>");
//        mailService.mail(sb.toString(), user.getEmail(), "Account aanvraag");
    }

    public Role findInactiveRole(List<String> roles) {
        for (String role : roles) {
            if (role.equals("inactive")) {
                return new Role(role);
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        em.joinTransaction();
        HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
        Session hses = hem.getSession();

        Query criteria = hses.createQuery("select u from User u");
        criteria.setCacheable(true);
        criteria.setCacheRegion("users");
        return criteria.list();
    }

    public List<String> getRoles() {

        return roleEAO.getRoles();
    }


    public User merge(User user) {
        if (user != null) {
            User old = userEAO.find(User.class, user.getId());
            if (user.getRole().getUsername() != user.getUsername()) {
                Role role = new Role(user.getRole().getName());
                role.setUsername(user.getUsername());
                roleEAO.persist(role);
                user.setRole(role);
            }
            String password = old.getPassword();
            user.setUnencryptedPassword(password);
            return userEAO.merge(user);
        }
        return null;
    }

    public class RegisterResponse {
        private String msg;

        public RegisterResponse(String content) {
            this.msg = content;
        }

        public String getMsg() {
            return msg;
        }

    }

    public ForgetMessage forgotPassword(String email) {
        if (email != null) {
            em.setProperty("skipFilter", true);
            TypedQuery<User> q = em.createNamedQuery("getUserByEmail",
                    User.class);
            q.setParameter("email", email);
            User user;
            List<User> users = q.getResultList();
            em.setProperty("skipFilter", false);
            if (users.size() > 0) {
                ForgetMessage forgetMessage = new ForgetMessage("ok");
                user = users.get(0);
                String password = RandomPasswordGenerator.generate();
                user.setPassword(password);
//                mailService.mail("Beste " + user.getFirstname() + " " + user.getSurname() + ", <br/>Uw wachtwoord is hersteld naar: " + password + "<br/><br/>Met vriendelijke groet,<br/>het ZZP maatschap team", user.getEmail(), "ZZPMaatschap wachtwoord hersteld");
                return forgetMessage;
            }
        }
        return new ForgetMessage("error");
    }
}

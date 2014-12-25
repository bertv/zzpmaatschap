package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.eao.inferface.IRoleEAO;
import com.bv.zzpmaatschap.eao.inferface.IUserEAO;
import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.Role;
import com.bv.zzpmaatschap.model.User;
import com.bv.zzpmaatschap.services.ForgetMessage;
import com.bv.zzpmaatschap.services.UserService;
import com.bv.zzpmaatschap.services.interfaces.IUserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */

@Stateless
@Path("/account")
public class AccountRESTService {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @EJB(name = "userService")
    UserService userService;

    @EJB(beanName = "userEAO", beanInterface = IUserEAO.class)
    IUserEAO userEAO;

    @EJB(beanName = "companyEAO", beanInterface = ICompanyEAO.class)
    ICompanyEAO companyEAO;

    @EJB(beanName = "roleEAO", beanInterface = IRoleEAO.class)
    IRoleEAO roleEAO;


    //    @RolesAllowed("admin")
    @GET
    @Path("/user")
    public User getAccount(
            @Context HttpServletRequest request) {
        return userService.getCurrentUser(request.getUserPrincipal());

    }

    @RolesAllowed("admin")
    @GET
    @Path("/allcaccounts")
    @Produces("application/json")
    public List<CAccount> getAccounts(
            @Context HttpServletRequest request) {
        List<Company> allCompanies = companyEAO.getAllCompanies();

        List<CAccount> cAccountList = new ArrayList<CAccount>();
        for (User user : userService.getAllUsers()) {
            CAccount cAccount = new CAccount(user,allCompanies.size(),new CRole(user.getRole()));
            int i = 0;
            for (Company company : allCompanies) {
                cAccount.getCompanyCheck()[i]=new CCompanyCheck(company,false);
                for (Company userComp : user.getCompanies()) {
                    if (userComp.getId() == company.getId()) {
                        cAccount.getCompanyCheck()[i]=new CCompanyCheck(userComp,true);
                        break;
                    }
                }
                i++;
            }
            cAccountList.add(cAccount);
        }
        return cAccountList;
    }


    @GET
    @Path("/forgot")
    @Produces("application/json")
    public ForgetMessage forgotPassword(
            @QueryParam(value = "email") String email) {
        return userService.forgotPassword(email);

    }

    @RolesAllowed("admin")
    @POST
    @Path("/save")
    @Consumes("application/json")
    @Produces("application/json")
    public User getRoles(CAccount cuser) {
        User user =userEAO.find(User.class,cuser.getId());
        List<Company> allcompanies=companyEAO.getAllCompanies();
        user.transfer(cuser,allcompanies);

        if (!user.getRole().getName().equals(cuser.getCRole().getName())){
            Role roleUsed=roleEAO.find(Role.class,user.getRole().getId());
            Role role = new Role();
            if (roleUsed!=null){
                role=roleUsed;
            }
            role.setUsername(cuser.getUsername());
            role.setName(cuser.getCRole().getName());
            user.setRole(role);
            if (role.getId()==null){
            roleEAO.persist(role);
            }else{
                roleEAO.merge(role);
            }
        }
        return userService.merge(user);
    }

    @RolesAllowed("admin")
    @GET
    @Path("/roles")
    @Produces("application/json")
    public List<CRole> getRoles() {
        List<CRole> cRoles=new ArrayList<>();
        for (String role: userService.getRoles()){

                cRoles.add(new CRole(role));
        }
        return cRoles;
    }

    private String validate(User user) {

        if (user.getUsername() != null) {
            if (user.getUsername().length() < 5) {
                return "Naam te kort (groter dan 5).";
            }
        } else {
            return "Geen naam ingevuld.";
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().matches(EMAIL_PATTERN)) {

                return "Geen geldig emailadres.";
            }
        } else {
            return "Geen emailadres ingevuld.";
        }
        return "";
    }

    @POST
    @Path("/selectCompany")
    @Consumes("application/json")
    @Produces("application/json")
    public void selectCompany(@Context HttpServletRequest request,Company selectedCompany){
        User user = userService.getCurrentUser(request.getUserPrincipal());
        Company company=companyEAO.find(Company.class,selectedCompany.getId());
        user.setDefaultCompany(company);
        userEAO.merge(user);
    }

    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public String register(
            User user) {
        String message = validate(user);
        if (message.isEmpty()) {
            userService.register(user);
        } else {
            return "{\"status\":\"not_valid\",\"message\":\"" + message + "\"}";
        }
        return "{\"status\":\"ok\"}";
    }
}

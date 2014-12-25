package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.CompanyEAO;
import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.eao.inferface.IUserEAO;
import com.bv.zzpmaatschap.model.Company;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.List;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */

@Stateless
@Path("/company")
public class CompanyRESTService {
    @EJB(beanName = "companyEAO", beanInterface = ICompanyEAO.class)
    ICompanyEAO companyEAO;
    @EJB(beanName = "userEAO", beanInterface = IUserEAO.class)
    IUserEAO userEAO;


    @RolesAllowed(value = "admin")
    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Company> allCompanies() {
        return companyEAO.getAllCompanies();
    }

    @RolesAllowed("admin")
    @GET
    @Path("/:id")
    @Consumes("application/json")
    public List<Company> allCompanies(@QueryParam(value = "id") Long offerId) {
        return companyEAO.getCompanies(offerId);
    }

    @RolesAllowed("admin")
    @POST
    @Path("/merge")
    @Consumes("application/json")
    @Produces("application/json")
    public Company merge(Company company) {
        return companyEAO.merge(company);
    }

    @RolesAllowed("admin")
    @POST
    @Path("/save")
    @Consumes("application/json")
    @Produces("application/json")
    public void save(Company company) {
        if (company.getId()!=null){
        companyEAO.merge(company);
        }else{
            companyEAO.persist(company);
        }
    }

    @RolesAllowed("admin")
    @POST
    @Path("/remove")
    @Consumes("application/json")
    @Produces("application/json")
    public String remove(Company company) {
        Company attached=companyEAO.find(Company.class,company.getId());
        boolean hasUsers =companyEAO.hasUsers(company);
        if (hasUsers){
            return "{\"status\":\"Er zijn nog gebruikers gekoppeld.\"}";
        }
        attached.setOffers(null);
        attached.setUsers(null);
        companyEAO.remove(attached);
        return "{\"status\":\"ok\"}";
    }

}

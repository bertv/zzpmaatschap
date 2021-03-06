package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.eao.inferface.IUserEAO;
import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.services.UserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */


@Stateless
@Path("/offers")
public class OfferRESTService {
    @EJB(name = "userService")
    UserService userService;

    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;

    @EJB(beanName = "itemService", beanInterface = IItemService.class)
    IItemService itemService;

    @EJB(beanName = "companyEAO", beanInterface = ICompanyEAO.class)
    ICompanyEAO companyEAO;

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Offer lookupMemberById(@PathParam("id") long id) {
        return offerEAO.find(Offer.class, id);
    }

    @GET
    @Path("/query")
    @Produces("application/json")
    public List<Offer> allOffers() {
        return offerEAO.getAllOffers();
    }
    @RolesAllowed("admin")
    @GET
    @Path("/tenant")
    @Produces("application/json")
    public List<OfferTenant> allTenantOffers() {
        return createNaked(offerEAO.getAllOffersNoRestrictions());
    }
    @RolesAllowed("admin")
    @POST
    @Path("/removeold")
    @Produces("application/json")
    public String deleteOld(String date) {
        try {
            DateFormat instance=SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
            Date olddate=instance.parse(date);

            offerEAO.removeOldOffers(olddate,itemService);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
    private List<OfferTenant> createNaked(List<Offer> allOffers) {
        List<Company> allCompanies=companyEAO.getAllCompanies();
        List<OfferTenant> offerNakedList=new ArrayList<>();
        for (Offer offer:allOffers){
            offerNakedList.add(new OfferTenant(offer,allCompanies));
        }
        return offerNakedList;
    }

    @POST
    @Path("/saveTenant")
    @Consumes("application/json")
    public void saveTenant(OfferTenant offer) {
        Offer attachedOffer=offerEAO.find(Offer.class,offer.getId());

        attachedOffer.setTenantId1(offer.getTenantId1()==null?null:offer.getTenantId1().getId().intValue());
        attachedOffer.setTenantId2(offer.getTenantId2()==null?null:offer.getTenantId2().getId().intValue());
        attachedOffer.setTenantId3(offer.getTenantId3()==null?null:offer.getTenantId3().getId().intValue());
        attachedOffer.setTenantId4(offer.getTenantId4()==null?null:offer.getTenantId4().getId().intValue());
        offerEAO.merge(attachedOffer);

    }

    public void setTenants(Offer offer,Company company){
        offer.setTenantId1(company.getId().intValue());
        offer.setTenantId2(company.getId().intValue());
        offer.setTenantId3(company.getId().intValue());
        offer.setTenantId4(company.getId().intValue());
    }


    @POST
    @Path("/delete")
    public void delete(Offer offer) {
        Offer attached = offerEAO.find(Offer.class, offer.getId());
        for (Item itemToClear:attached.getItems()){
            itemToClear.setOffer(null);
            itemService.merge(itemToClear);
        }
        offerEAO.remove(attached);

    }

    @POST
    @Path("/copy")
    @Consumes("application/json")
    public void copyOffer(Offer offer) {
        Offer attached = offerEAO.find(Offer.class, offer.getId());
        Offer newOffer = new Offer();
        attached.copy(newOffer);

        offerEAO.persist(newOffer);

    }

    @POST
    @Path("/save")
    @Consumes("application/json")
    public void postPersist(Offer offer, @Context HttpServletRequest request) {
        Company company;
        if (offer.getId() == null) {
            if (offer.getCompany()==null){
                company=userService.getCurrentUser(request.getUserPrincipal()).getDefaultCompany();
                if (company!=null){
                    offer.setCompany(company);
                }else{
                    offer.setCompany(companyEAO.getCompanies(offer.getId()).get(0));
                }
            }else{
                company=offer.getCompany();
            }
            setTenants(offer,company);
            offerEAO.persist(offer);
            return;
        }
        offerEAO.merge(offer);

    }

}

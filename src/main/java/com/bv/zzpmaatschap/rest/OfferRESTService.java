package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.ICompanyEAO;
import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.Offer;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members
 * table.
 */


@Stateless
@Path("/offers")
public class OfferRESTService {


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

//    @POST
//    @Path("/save")
//    @Consumes("application/json")
//    public void postMerge(Offer offer) {
//        offerEAO.merge(offer);
//
//    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}/delete")
    public void delete(@PathParam("id") long id) {
        Offer attached = offerEAO.find(Offer.class, id);
        for (Item itemToClear:attached.getItems()){
            itemToClear.setOffer(null);
            itemService.merge(itemToClear);
        }
        offerEAO.remove(attached);

    }

    @POST
    @Path("/{id:[0-9][0-9]*}/copy")
    @Consumes("application/json")
    public void copyOffer(@PathParam("id") long id) {
        Offer attached = offerEAO.find(Offer.class, id);
        Offer newOffer = new Offer();
        attached.copy(newOffer);

        offerEAO.persist(newOffer);

    }

    @POST
    @Path("/save")
    @Consumes("application/json")
    public void postPersist(Offer offer) {
        if (offer.getId() == null) {
            offerEAO.persist(offer);
            return;
        }
        offerEAO.merge(offer);

    }

}

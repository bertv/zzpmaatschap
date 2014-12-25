package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.eao.inferface.IMaterialItemEAO;
import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.eao.inferface.IWorkItemEAO;
import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.MaterialItem;
import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.WorkItem;

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
@Path("/items")
public class ItemRESTService {
    @EJB(beanName = "itemService", beanInterface = IItemService.class)
    IItemService itemService;

    @EJB(beanName = "offerEAO", beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;

    @EJB(beanName = "workItemEAO", beanInterface = IWorkItemEAO.class)
    IWorkItemEAO workItemEAO;

    @EJB(beanName = "materialItemEAO", beanInterface = IMaterialItemEAO.class)
    IMaterialItemEAO materialItemEAO;


    @GET
    @Path("/query/{offerid:[0-9][0-9]*}")
    @Produces("application/json")
    public List<Item> allItemsForOffer(@PathParam("offerid") long id) {
        return itemService.getOrderedItems(offerEAO.find(Offer.class, id));
    }

    @GET
    @Path("/query")
    @Produces("application/json")
    public List<Item> allItems() {
        return itemService.getAllItems();
    }


    @DELETE
    @Path("/delete")
    public void delete(@QueryParam("id") long id) {
        Item attached = itemService.find(Item.class, id);

        for (MaterialItem mi : attached.getMaterials()) {
            materialItemEAO.remove(mi);

        }
        attached.getMaterials().clear();
        for (WorkItem wi : attached.getWorks()) {
            workItemEAO.remove(wi);

        }
        attached.getWorks().clear();
        //attached.setWorks(null);
        attached.getOffer().getItems().remove(attached);
        attached.setOffer(null);
        itemService.remove(attached);

    }

    @POST
    @Path("/merge")
    @Consumes("application/json")
    @Produces("application/json")
    public Item postPersist(Item item) {

        if (item.getId() == null) {
            itemService.persist(item);
            return item;
        }
//        Item attached=itemService.find(Item.class,item.getId());
//        item.setOffer(attached.getOffer());
        Item mergedItem = itemService.merge(item);
        for (WorkItem wi : item.getWorks()) {
//            wi.setItem(mergedItem);
            workItemEAO.merge(wi);

        }
        return mergedItem;

    }
}

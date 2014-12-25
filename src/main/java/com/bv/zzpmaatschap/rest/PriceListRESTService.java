package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IPriceListEAO;
import com.bv.zzpmaatschap.eao.inferface.IPriceListItemEAO;
import com.bv.zzpmaatschap.model.pricelist.PriceList;
import com.bv.zzpmaatschap.model.pricelist.PriceListItem;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.List;


@Stateless
@Path("/pricelist")
public class PriceListRESTService {


    @EJB(beanName = "priceListEAO", beanInterface = IPriceListEAO.class)
    IPriceListEAO priceListService;

    @EJB(beanName = "priceListItemEAO", beanInterface = IPriceListItemEAO.class)
    IPriceListItemEAO priceListItemService;


    @GET
    @Path("/query")
    @Produces("application/json")
    public List<PriceList> getAllPriceLists() {
        return priceListService.getAllPriceLists();
    }

    @DELETE
    @Path("/delete")
    public void removePricelist(@QueryParam("pricelist") long id) {//@PathParam("id") long id
        priceListService.remove(id);
    }

    @GET
    @Path("/count")
    @Produces("application/json")
    public Size getPriceListItemCount(@QueryParam("pricelist") long id) {//@PathParam("id") long id

        return new Size(priceListItemService.getPriceListItemCount(id));
    }

    @GET
    @Path("/search")
    @Produces("application/json")
    public List<PriceListItem> getPriceListItems(@QueryParam("name") String name, @QueryParam("pricelist") long id) {
        return priceListItemService.getPriceListItemsByName(name);
    }


}

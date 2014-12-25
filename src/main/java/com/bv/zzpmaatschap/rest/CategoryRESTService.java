package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.CategoryService;
import com.bv.zzpmaatschap.eao.ItemService;
import com.bv.zzpmaatschap.eao.OfferEAO;
import com.bv.zzpmaatschap.eao.inferface.ICategoryEAO;
import com.bv.zzpmaatschap.eao.inferface.IItemService;
import com.bv.zzpmaatschap.eao.inferface.IOfferEAO;
import com.bv.zzpmaatschap.model.Category;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.List;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */

@Stateless
@Path("/category")
public class CategoryRESTService {
	@EJB(beanName = "itemService",beanInterface = IItemService.class)
    IItemService itemService;

    @EJB(beanName = "offerEAO",beanInterface = IOfferEAO.class)
    IOfferEAO offerEAO;


    @EJB(beanName = "categoryEAO",beanInterface = ICategoryEAO.class)
    ICategoryEAO categoryEAO;

//	@GET
//	@Path("/{id:[0-9][0-9]*}")
//	@Produces("application/json")
//	public Item getById(@PathParam("id") long id) {
//		return itemService.find(Item.class, id);
//	}

    @POST
    @Path("/save")
    @Consumes("application/json")
    public Category merge(Category category) {
    if (category.getId()==null){
            categoryEAO.persist(category);
        return category;
    }
        return categoryEAO.merge(category);


    }
    @GET
    @Path("/query")
    @Produces("application/json")
    public List<Category> allItems() {
        return categoryEAO.getAllCategories();
    }


}

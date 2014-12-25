package com.bv.zzpmaatschap.eao.inferface;

import java.util.List;

import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.Offer;

public interface IItemService extends EAO<Item>{
	public List<Item> getChangedItems(Offer offer);
	public List<Item> getOrderedItems(Offer offer);
	
	public List<Item> getDescriptions(Offer offer, String query);
	
	public Item getItemByDescription(Offer offer, String description);

    public List<Item> getAllItems();
}

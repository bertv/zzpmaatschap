package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.pricelist.PriceListItem;

import java.util.List;

public interface IPriceListItemEAO extends EAO<PriceListItem>{

    public List<PriceListItem> getPriceListItemsByName(String name);

    long getPriceListItemCount(long id);
}

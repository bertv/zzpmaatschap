package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.pricelist.PriceList;

import java.util.List;

public interface IPriceListEAO extends EAO<PriceList>{

    public List<PriceList> getAllPriceLists();

    public void remove(Long id);


}

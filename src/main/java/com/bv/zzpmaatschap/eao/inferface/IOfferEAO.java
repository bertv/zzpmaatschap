package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.Offer;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

public interface IOfferEAO extends EAO<Offer> {

	List<Offer> getAllOffers();

    List<Offer> getAllOffersNoRestrictions();

    List<Offer> getUserOffers(Long userId);

    void removeOldOffers(Date olddate, IItemService itemService);
}

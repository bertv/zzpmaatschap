package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.WorkTariff;

import java.util.List;

public interface IWorkTariffEAO extends EAO<WorkTariff>{


    List<WorkTariff> getAllTariffs();
}

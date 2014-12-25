package com.bv.zzpmaatschap.rest;

import com.bv.zzpmaatschap.eao.inferface.IWorkTariffEAO;
import com.bv.zzpmaatschap.model.WorkTariff;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;


@Stateless
@Path("/tariff")
public class TariffRESTService {

    @EJB(beanName = "workTariffEAO", beanInterface = IWorkTariffEAO.class)
    IWorkTariffEAO workTariffEAO;


    @RolesAllowed({"inactive","admin","user_temp"})
    @GET
    @Path("/all")
    @Produces("application/json")
    public List<WorkTariff> uploadFile() {
        return workTariffEAO.getAllTariffs();
    }
}

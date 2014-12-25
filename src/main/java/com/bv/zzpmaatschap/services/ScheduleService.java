package com.bv.zzpmaatschap.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;

import com.bv.zzpmaatschap.management.persistence.MultiTenantEntityManagerWrapper;

@Stateless
public class ScheduleService {
	@EJB(beanName = "mtem", beanInterface = MultiTenantEntityManagerWrapper.class)
	// @PersistenceContext
	private EntityManager em;

	//@Schedule(second = "0", hour = "0", minute = "*/2")
	public void checkAccountExpiration(Timer t) {
		
		System.out.println("Check expiration of accounts.");
	}
}

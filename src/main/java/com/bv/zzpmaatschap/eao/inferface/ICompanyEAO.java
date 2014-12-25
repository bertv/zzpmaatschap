package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.Company;
import com.bv.zzpmaatschap.model.Offer;

import java.util.List;

public interface ICompanyEAO extends EAO<Company> {

	List<Company> getAllCompanies();

    List<Company> getCompanies(Long offerId);

    boolean hasUsers(Company company);
}

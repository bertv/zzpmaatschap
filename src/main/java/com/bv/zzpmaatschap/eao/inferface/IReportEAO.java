package com.bv.zzpmaatschap.eao.inferface;

import com.bv.zzpmaatschap.model.Offer;
import com.bv.zzpmaatschap.model.WorkItem;
import com.bv.zzpmaatschap.model.report.Report;

import java.util.List;

public interface IReportEAO extends EAO<Report>{


    List<Report> getReports(Offer offer, Long offerId);
}

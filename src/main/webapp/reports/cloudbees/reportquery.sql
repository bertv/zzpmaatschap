select o.location offer_location, o.name offer_name, i.description item_desc,c.description cat_description,mi.price mi_price, mi.name mi_desc, mi.quantity mi_quantity,qu.name qu_name, wi.description wi_desc, wi.workMinutes wi_minutes, wt.value wt_value, o.address offer_address, o.BTWwage BTWloon,o.BTWadditional BTWoverig, o.email email,o.applicant aanvrager,o.nameArchitect naamarchitect, o.parkingCosts parkeerkosten, o.profitAndRisk winstrisico, o.provisionalUnforeseen postonvoorzien,o.telephoneCosts telefoonkosten,o.dateRequest datumindienen, o.dateInvoice datumfactuur, i.size aantal,i.category_id commentaar
from item i
inner join offer o on o.id=i.offer_id
inner join item_materialitem im on i.id=im.item_id
inner join materialitem mi on mi.id=im.materials_id
inner join category c on i.category_id=c.id
inner join quantityunit qu on qu.id=mi.type_id
inner join item_workitem iw on iw.item_id=i.id
inner join workitem wi on wi.id=iw.works_id
inner join worktariff wt on wi.tariff_id = wt.id
#left join report rp on rp.offer_id = o.id
#left join reportparameter rpp on rpp.report_id=rp.id
where i.offer_id=$P{p_offer_id}
order by c.description,i.orderNumber,i.id
create index INDX_OFFERID on zzpmaatschap2.item (id, offer_id);
create index INDX_OFF_COMP_ID on zzpmaatschap2.offer (id, Company_id);
create index INDX_OFF_TENT_ID on zzpmaatschap2.offer (id, tenant_id1,tenant_id2,tenant_id3,tenant_id4);
create index INDX_OFF_MAT_WORK_ID on zzpmaatschap2.item (id, offer_id,category_id,type_id);
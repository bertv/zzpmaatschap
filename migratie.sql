
delete from zzpmaatschap2.item_materialitem;
delete from zzpmaatschap2.materialitem;

delete from zzpmaatschap2.item_workitem;
delete from zzpmaatschap2.workitem;
delete from zzpmaatschap2.worktariff;
delete from zzpmaatschap2.item;
delete from zzpmaatschap2.category;
delete from zzpmaatschap2.report;
delete from zzpmaatschap2.offer;
delete from zzpmaatschap2.itemtype;

INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('5');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('10');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('15');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('20');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('25');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('30');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('35');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('40');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('45');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('50');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('55');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('60');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('65');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('70');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('75');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('80');
INSERT INTO zzpmaatschap2.`worktariff` (`value`) VALUES ('0');

INSERT INTO zzpmaatschap2.`itemtype` (`id`,`description`) VALUES (1,'standaard');
INSERT INTO zzpmaatschap2.`itemtype` (`id`,`description`) VALUES (2,'stelpost');
INSERT INTO zzpmaatschap2.`itemtype` (`id`,`description`) VALUES (3,'onderaanneming');

SET lc_time_names = 'nl_NL';
#SET @@SESSION.sql_mode='ALLOW_INVALID_DATES';
DROP PROCEDURE IF EXISTS  migratie;
DELIMITER $$
 
CREATE PROCEDURE migratie()

BEGIN

 DECLARE naam_project,locatie,aanvrager,architect,parkeerkosten,telefoonkosten,risico,onvoorzien,btw_loon,btw_overig,status_offerte,datum_indienen,datum_aanvraag,adres
  VARCHAR(250);

DECLARE offerte_id varchar(250);
DECLARE lastOfferId int(10);
DECLARE lastItemId int(10);
 declare no_more_rows1 boolean;  
 
    DECLARE cur CURSOR FOR 
select ind.naam_project naam_project,ind.locatie locatie,ind.aanvrager aanvrager,ind.architect,ind.parkeerkosten,telefoonkosten,risico,onvoorzien,btw_loon,btw_overig,ind.status,ind.datum_indienen,ind.datum_aanvraag,ind.adres ,ind.offerte_id offerte_id
from zzpmaats.mkc_offerte_index ind
limit 4;


 declare continue handler for not found  set no_more_rows1 := TRUE; 

OPEN cur;
  
cur:LOOP
  
FETCH cur INTO naam_project,locatie,aanvrager,architect,parkeerkosten,telefoonkosten,risico,onvoorzien,btw_loon,btw_overig,status_offerte,datum_indienen,datum_aanvraag,adres,offerte_id;
 if no_more_rows1 then
            close cur;
            leave cur;
        end if;
set datum_aanvraag=NOW();
set datum_indienen=NOW();

insert into zzpmaatschap2.offer (company_id,tenant_id,creationDate,name,location,applicant,nameArchitect,parkingCosts,telephoneCosts,profitAndRisk,provisionalUnforeseen,BTWwage,BTWadditional,status,dateInvoice,dateRequest,address)  
values (1,'c1n2n3',NOW(),naam_project,locatie,aanvrager,architect,parkeerkosten,telefoonkosten,risico,onvoorzien,btw_loon,btw_overig,status_offerte,datum_indienen,datum_aanvraag,adres)
on duplicate key update name=CONCAT(name,location);

set lastOfferId=LAST_INSERT_ID();
ITEMBLOCK: BEGIN
DECLARE quId int(10);
DECLARE categorie,omschrijving,eenheid,prijs,norm_tijd,opmerking,loon_tarief,aantal VARCHAR(250);
  declare no_more_rows2 boolean;
DECLARE curItem CURSOR FOR select of.categorie,of.omschrijving,of.eenheid,of.prijs,of.norm_tijd,of.opmerking,of.loon_tarief,of.aantal from zzpmaats.mkc_offerte of where of.offerte_id=offerte_id ;
 
 declare continue handler for not found             set no_more_rows2 := TRUE;
OPEN curItem; 
        curItem: LOOP

        FETCH FROM curItem INTO categorie,omschrijving,eenheid,prijs,norm_tijd,opmerking,loon_tarief,aantal;   
			if no_more_rows2 then
                    close curItem;
                    leave curItem;
                end if;

			if omschrijving is null then
			set omschrijving=categorie+'(2)';
			end if;
			if categorie = '' then
			set categorie='onbekend';
			end if;
			insert ignore into zzpmaatschap2.category (description) values(categorie);

			insert  into zzpmaatschap2.item (description,offer_id,category_id,type_id,size,orderNumber,popularity)
			values (omschrijving,lastOfferId,(select id from zzpmaatschap2.category where description=categorie),(select id from zzpmaatschap2.itemtype it where it.description='standaard'),aantal,1,1)
			on duplicate key update description=CONCAT(omschrijving,categorie);

			set lastItemId= LAST_INSERT_ID();
			set quId=(select id from zzpmaatschap2.quantityunit where name like CONCAT(CONCAT('%',eenheid),'%') limit 1);
			if (quId is null) then
				set quId=1;
			end if;
			insert  into zzpmaatschap2.materialitem (name,price,quantity,type_id,comment) 
			values (omschrijving,prijs,1,quId,opmerking);
			insert  into zzpmaatschap2.item_materialitem (Item_id,materials_id) 
			values (lastItemId,LAST_INSERT_ID());
			

			insert  into zzpmaatschap2.workitem (tariff_id,workMinutes) 
			values ((select wt.id from zzpmaatschap2.worktariff wt where wt.value=loon_tarief),norm_tijd);
			insert  into zzpmaatschap2.item_workitem (Item_id,works_id) 
			values (lastItemId,LAST_INSERT_ID());
	
	END LOOP curItem;
	

	END ITEMBLOCK;



END LOOP cur;   
UPDATE `zzpmaatschap2`.`item` SET orderNumber = id;
END$$

call migratie();



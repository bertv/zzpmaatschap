alter table item drop column tenant_id; 
UPDATE `zzpmaatschap2`.`item` SET orderNumber = id;
UPDATE `zzpmaatschap2`.`offer` SET tenant_id = 'c1n2n3';

#ALTER TABLE `zzpmaatschap2`.cb_users DROP PRIMARY KEY;

#ALTER TABLE `zzpmaatschap2`.cb_users ADD constraint PK_ID PRIMARY KEY (id);

drop table `zzpmaatschap2`.cb_users;

drop table `zzpmaatschap2`.cb_groups;

INSERT INTO `cb_users`
(`id`,`username`,
`password`,`creationDate`,`email`)
VALUES
(1,'myuser1','x3Xnt1ft5jDNCqERO9ECZhqziCnKUqZCKreChi8mhkY=','2014-10-10','testmyuser1@bertverhelst.nl');


INSERT INTO `cb_users`
(`id`,`username`,
`password`,`creationDate`,`email`)
VALUES
(2,'admin','x3Xnt1ft5jDNCqERO9ECZhqziCnKUqZCKreChi8mhkY=','2014-10-10','testadmin@bertverhelst.nl');

INSERT INTO `zzpmaatschap2`.`cb_users_company` (`cb_users_id`, `companies_id`) VALUES ('1', '1');

INSERT INTO `zzpmaatschap2`.`company_cb_users` (`Company_id`, `users_id`) VALUES ('2', '2');

INSERT INTO `zzpmaatschap2`.`cb_groups` (`groupname`, `username`) VALUES ('user_temp', 'myuser1');
INSERT INTO `zzpmaatschap2`.`cb_groups` (`groupname`, `username`) VALUES ('admin','admin');
UPDATE `zzpmaatschap2`.`cb_users` SET `role_id`='1' WHERE `id`='1';
UPDATE `zzpmaatschap2`.`cb_users` SET `role_id`='2' WHERE `id`='2';

#ALTER TABLE cb_groups DROP INDEX `UK_l4c497tatx6fwmlwgqgktop99`;
#alter table item drop foreign key FK_awm49opbvuinpi5139jk6qbpt;

UPDATE `zzpmaatschap2`.`offer` SET `tenant_id1`='1';
#drop column offer tenantId

#ALTER TABLE 'offer' DROP 'tenant_id';

#alter table cb_groups drop key UK_l4c497tatx6fwmlwgqgktop99;

ALTER TABLE offer ADD INDEX (tenant_id1);
ALTER TABLE offer ADD INDEX (tenant_id2);
ALTER TABLE offer ADD INDEX (company_id);
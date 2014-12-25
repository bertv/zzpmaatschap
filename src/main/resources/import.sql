#insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')
#insert into Offer (id,name) values (0,'DMD')
INSERT INTO `Company` (`name`,`streetname`,`number`,`city`,`postalcode`,`country`,`phoneNumber`) VALUES ('BedrijfGebruiker1','straatnaam','1','plaatsnaam','1234AB','Nederland','030-9038723');
INSERT INTO `Company` (`name`) VALUES ('BedrijfAdmin');
INSERT INTO `Company` (`name`) VALUES ('BedrijfTijdelijkeGebruiker');

INSERT INTO `User` (`name`, `password`, `email`, `creationDate`,`surname`,`firstname`,`postalcode`,`streetname`,`country`) VALUES ('myuser1', '1234567890', 'zzpuser1@bertverhelst.nl', '2014-10-16 10:10:10','Achternaam','Piet','1234AB','Bakkerstraat','Nederland');
INSERT INTO `User` (`name`, `password`, `email`, `creationDate`,`surname`,`firstname`,`postalcode`,`streetname`,`country`) VALUES ('admin', '1234567890', 'zzpadmin@bertverhelst.nl', '2018-10-16 10:10:10','Admin','Piet','1234AD','Hoogstraat','Nederland');
INSERT INTO `User` (`name`, `password`, `email`, `creationDate`,`surname`,`firstname`,`postalcode`,`streetname`,`country`) VALUES ('usertemp', '1234567890', 'zzptemp@bertverhelst.nl', '2014-10-16 10:10:10','Tijdelijk','Jan','1234TT','Tijdelijk te gast','Nederland');


INSERT INTO `Role` (`name`) VALUES ('user');
INSERT INTO `Role` (`name`) VALUES ('admin');
INSERT INTO `Role` (`name`) VALUES ('user_temp');
INSERT INTO `role_user` (`role_id`, `users_id`) VALUES ('1', '1');
INSERT INTO `user_role` (`roles_id`, `user_id`) VALUES ('1', '1');

INSERT INTO `role_user` (`role_id`, `users_id`) VALUES ('2', '2');
INSERT INTO `user_role` (`roles_id`, `user_id`) VALUES ('2', '2');


INSERT INTO `role_user` (`role_id`, `users_id`) VALUES ('3', '3');
INSERT INTO `user_role` (`roles_id`, `user_id`) VALUES ('3', '3');

INSERT INTO `user_company` (`User_id`, `companies_id`) VALUES ('1', '1');

INSERT INTO `user_company` (`User_id`, `companies_id`) VALUES ('2', '1');
INSERT INTO `user_company` (`User_id`, `companies_id`) VALUES ('2', '2');
INSERT INTO `user_company` (`User_id`, `companies_id`) VALUES ('2', '3');

INSERT INTO `user_company` (`User_id`, `companies_id`) VALUES ('3', '3');

INSERT INTO `offer` (`tenant_id`, `name`, `creationDate`, `location`, `status`, `dateRequest`,`company_id`,`address`,`applicant`,`email`,`nameArchitect`,`provisionalUnforeseen`,`BTWwage`,`BTWadditional`) VALUES ('c1', 'test', '2014-10-10 10:10:10', 'Amsterdam', 'nieuw', '2014-10-10 10:10:10','1','adres','aanvrager','email','architect','onvoorzien','btw loon','btw overig');

INSERT INTO `category` (`description`) VALUES ('timmerwerk');
INSERT INTO `category` (`description`) VALUES ('installatie');
INSERT INTO `category` (`description`) VALUES ('advies');

INSERT INTO `itemtype` (`description`) VALUES ('standaard');
INSERT INTO `itemtype` (`description`) VALUES ('stelpost');
INSERT INTO `itemtype` (`description`) VALUES ('onderaanneming');

INSERT INTO `item` (`tenant_id`, `description`, `category_id`, `offer_id`, `orderNumber`, `type_id`, `popularity`,`size`) VALUES ('1', 'kast maken', '1', '1', '1', '1', '0','1');
INSERT INTO `worktariff` (`value`) VALUES ('20');
INSERT INTO `worktariff` (`value`) VALUES ('25');
INSERT INTO `worktariff` (`value`) VALUES ('30');
INSERT INTO `worktariff` (`value`) VALUES ('35');
INSERT INTO `worktariff` (`value`) VALUES ('40');
INSERT INTO `worktariff` (`value`) VALUES ('45');
INSERT INTO `worktariff` (`value`) VALUES ('50');
INSERT INTO `worktariff` (`value`) VALUES ('55');
INSERT INTO `worktariff` (`value`) VALUES ('60');
INSERT INTO `worktariff` (`value`) VALUES ('65');
INSERT INTO `worktariff` (`value`) VALUES ('70');
INSERT INTO `worktariff` (`value`) VALUES ('75');
INSERT INTO `worktariff` (`value`) VALUES ('80');

INSERT INTO `pricelist` ( `name`,`total`) VALUES ( 'test','1');
INSERT INTO `pricelistitem`(  `amount`,  `itemNumber`,  `name`,  `price`,  `unit`,  `priceList_id`) VALUES  (    '1',    '1000',    'schroeven',    '2',    'per stuk',    '1'  );

INSERT INTO `workitem` (`description`, `workMinutes`, `tariff_id`) VALUES ('timmeren', '20', '1');
INSERT INTO `item_workitem` (`Item_id`, `works_id`) VALUES (1, 1);
INSERT INTO `quantityunit` (`name`) VALUES ('per stuk');
INSERT INTO `quantityunit` (`name`) VALUES ('per m');
INSERT INTO `quantityunit` (`name`) VALUES ('per m2');
INSERT INTO `quantityunit` (`name`) VALUES ('per m3');
INSERT INTO `quantityunit` (`name`) VALUES ('per cm');
INSERT INTO `quantityunit` (`name`) VALUES ('per cm2');
INSERT INTO `quantityunit` (`name`) VALUES ('per cm3');
INSERT INTO `quantityunit` (`name`) VALUES ('per mm');
INSERT INTO `quantityunit` (`name`) VALUES ('per doos');
INSERT INTO `quantityunit` (`name`) VALUES ('per plaat');
INSERT INTO `quantityunit` (`name`) VALUES ('per emmer');
INSERT INTO `quantityunit` (`name`) VALUES ('per kan');
INSERT INTO `quantityunit` (`name`) VALUES ('per pak');
INSERT INTO `quantityunit` (`name`) VALUES ('per fles');
INSERT INTO `quantityunit` (`name`) VALUES ('per rol');
INSERT INTO `quantityunit` (`name`) VALUES ('per zak');
INSERT INTO `quantityunit` (`name`) VALUES ('per flacon');
INSERT INTO `quantityunit` (`name`) VALUES ('per bus');
INSERT INTO `materialitem` (`price`, `name`, `quantity`, `type_id`) VALUES (10, 'spijkers', 1, 1);

INSERT INTO `item_materialitem` (`Item_id`, `materials_id`) VALUES (1, 1);

#INSERT INTO `category_item` (`Category_id`, `items_id`) VALUES ('1', '1');
CREATE TABLE `cb_users` (
 `username` varchar(255) NOT NULL,
 `password` varchar(255) DEFAULT NULL,
 PRIMARY KEY (`username`)
);

CREATE TABLE `cb_groups` (
 `groupname` varchar(255) NOT NULL,
 `username` varchar(255) NOT NULL,
 `ID` int(11) NOT NULL AUTO_INCREMENT,
 PRIMARY KEY (`ID`),
 UNIQUE KEY `ID_UNIQUE` (`ID`)
) ;
 INSERT INTO `cb_users`
(`username`,
`password`)
VALUES
('myuser1','x3Xnt1ft5jDNCqERO9ECZhqziCnKUqZCKreChi8mhkY=');
INSERT INTO `cb_groups`
(`groupname`,
`username`,
`ID`)
VALUES
('user','myuser1','1');

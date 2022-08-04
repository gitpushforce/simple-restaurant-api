CREATE TABLE IF NOT EXISTS ORDER_TBL (
    order_id INT NOT NULL AUTO_INCREMENT,
    table_num INT NOT NULL,
    item_id varchar(4) NOT NULL,
    cook_time INT NOT NULL,
    update_date TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_date timestamp NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS ITEMS_TBL (
    item_id varchar(4) NOT NULL,
    item_name varchar(20) NOT NULL,
    PRIMARY KEY (item_id)
);

INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0001', 'item1');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0002', 'item2');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0003', 'item3');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0004', 'item4');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0005', 'item5');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0006', 'item6');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0007', 'item7');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0008', 'item8');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0009', 'item9');
INSERT INTO `restaurant`.`ITEMS_TBL` (`item_id`, `item_name`) VALUES ('0010', 'item10');

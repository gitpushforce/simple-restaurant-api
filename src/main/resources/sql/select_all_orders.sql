SELECT
  o.order_id as order_id,
  o.table_num as table_num,
  o.item_id as item_id,
  i.item_name as item_name,
  o.cook_time as cook_time
FROM
  ORDER_TBL as o
  INNER JOIN ITEMS_TBL as i
  ON o.item_id = i.item_id
WHERE
  o.table_num = :table_num
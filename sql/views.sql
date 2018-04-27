create or replace view top_selling_products
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
group by product_id, product_name
order by amount_sold desc

create or replace view warehouse_stock
as
select product_id, product_name, qty, unit_price
from (
    select product_id, sum(qty) as qty, avg(unit_price) as unit_price
    from stock natural join location
    where location_id in (select location_id from warehouse)
    group by product_id
)
natural join product
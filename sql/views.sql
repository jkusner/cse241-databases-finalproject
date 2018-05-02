-- Top Selling Products --

create or replace view top_selling_products
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
group by product_id, product_name
order by amount_sold desc;

create or replace view top_selling_products_year
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '1' year)
group by product_id, product_name
order by amount_sold desc;

create or replace view top_selling_products_month
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '1' month)
group by product_id, product_name
order by amount_sold desc;

create or replace view top_selling_products_day
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '1' day)
group by product_id, product_name
order by amount_sold desc;

-- Warehouse Stock --

create or replace view warehouse_stock
as
select product_id, product_name, qty, unit_price
from (
    select product_id, sum(qty) as qty, unit_price
    from stock natural join warehouse
    group by product_id, unit_price
)
natural join product
order by unit_price
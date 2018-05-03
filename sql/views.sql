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

create or replace view top_selling_products_quarter
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '3' month)
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
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '7' day)
group by product_id, product_name
order by amount_sold desc;

create or replace view top_selling_products_day
as
select product_id, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold, product_name
from purchased natural join product
where transaction_id in (select transaction_id from transaction where timestamp > current_timestamp - interval '1' day)
group by product_id, product_name
order by amount_sold desc;

-- Sales Totals --

create or replace view sales_totals_per_day
as
select to_char(timestamp, 'MON D, YYYY') as date_str, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold,
    count(distinct transaction_id) as num_trans
from transaction inner join purchased using (transaction_id)
group by to_char(timestamp, 'YYYY/MM/DD'), to_char(timestamp, 'MON D, YYYY')
order by to_char(timestamp, 'YYYY/MM/DD') desc;

create or replace view sales_totals_per_week
as
select 'Week ' || to_char(timestamp, 'WW, YYYY') as date_str, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold,
    count(distinct transaction_id) as num_trans
from transaction inner join purchased using (transaction_id)
group by 'Week ' || to_char(timestamp, 'WW, YYYY'), to_char(timestamp, 'YYYY/WW')
order by to_char(timestamp, 'YYYY/WW') desc;

create or replace view sales_totals_per_month
as
select to_char(timestamp, 'MON YYYY') as date_str, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold,
    count(distinct transaction_id) as num_trans
from transaction inner join purchased using (transaction_id)
group by to_char(timestamp, 'MON YYYY'), to_char(timestamp, 'YYYY/MM')
order by to_char(timestamp, 'YYYY/MM') desc;

create or replace view sales_totals_per_quarter
as
select 'Q' || to_char(timestamp, 'Q, YYYY') as date_str, sum(qty * unit_price) as total_sales, sum(qty) as amount_sold,
    count(distinct transaction_id) as num_trans
from transaction inner join purchased using (transaction_id)
group by 'Q' || to_char(timestamp, 'Q, YYYY'), to_char(timestamp, 'YYYY/Q')
order by to_char(timestamp, 'YYYY/Q') desc;

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
order by unit_price;

-- Store Transactions --

create or replace view store_transactions
as
select location_id, transaction_id, total, subtotal, tax, timestamp, 1 as isPickup
from transaction natural join pickup_order
union
select location_id, transaction_id, total, subtotal, tax, timestamp, 0 as isPickup 
from transaction natural join physical_transaction;
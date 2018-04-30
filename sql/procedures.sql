create or replace procedure purchase_product
    ( wanted_product_id in number,
      wanted_qty in number,
      wanted_unit_price in number,
      qty_got out number,
      total_paid out number )
as
    remaining number := wanted_qty;
    cursor findStock is
        select location_id, product_id, qty, unit_price
        from stock inner join warehouse using (location_id)
        where qty > 0 and product_id = wanted_product_id and unit_price <= wanted_unit_price
        order by unit_price;
    stockRow stock%rowtype;
begin
    qty_got := 0;
    total_paid := 0;
    
    dbms_output.put_line('Starting purchase');
    
    for row in findStock
    loop
        dbms_output.put_line('Remaining: ' || remaining || ', stock@' || row.location_id || ': ' || row.qty);
        if row.qty >= remaining
        then
            -- This location has enough stock to fulfill our request
            qty_got := qty_got + remaining;
            total_paid := total_paid + row.unit_price * remaining;
            
            update stock
            set qty = row.qty - remaining
            where location_id = row.location_id and product_id = wanted_product_id;
            
            remaining := 0;
            dbms_output.put_line('Finished purchase at ' || row.location_id);
        else
            -- We are purchasing all the stock at this location
            qty_got := qty_got + row.qty;
            total_paid := total_paid + row.unit_price * row.qty;
            remaining := remaining - row.qty;
            
            update stock
            set qty = 0
            where location_id = row.location_id and product_id = wanted_product_id;
            
            dbms_output.put_line('Bought out all stock at ' || row.location_id);
        end if;
        
        exit when remaining = 0;
    end loop;
    
    dbms_output.put_line('QTY_GOT: ' || qty_got || ', TOT_PAID: ' || total_paid || ', REMAINING: ' || remaining);
end;
create or replace procedure purchase_product
    ( trans_id in number,
      loc_id in number, /* null means warehouse purchase */
      wanted_product_id in number,
      wanted_qty in number,
      wanted_unit_price in number,
      qty_got out number,
      total_paid out number )
as
    remaining number := wanted_qty;
    cursor findStock is
        select location_id, product_id, qty, unit_price
        from stock
        where
            qty > 0 and product_id = wanted_product_id and unit_price <= wanted_unit_price
            and (location_id = loc_id
                or (loc_id is null and location_id in (select location_id from warehouse)))
        order by unit_price;
    stockRow stock%rowtype;
begin
    qty_got := 0;
    total_paid := 0;
    
    dbms_output.put_line('Starting purchase for transaction_id ' || trans_id);
    
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
    
    if qty_got > 0
    then
        insert into purchased 
            (transaction_id, product_id, qty, unit_price)
        values
            (trans_id, wanted_product_id, qty_got, total_paid / qty_got);
            
        update transaction
        set
            subtotal = subtotal + total_paid,
            total = subtotal + total_paid
        where
            transaction_id = trans_id;
    end if;
    
    dbms_output.put_line('QTY_GOT: ' || qty_got || ', TOT_PAID: ' || total_paid || ', REMAINING: ' || remaining);
end;
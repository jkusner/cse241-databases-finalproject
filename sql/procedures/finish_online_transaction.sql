create or replace procedure finish_online_transaction
    ( trans_id in number,
      tax_rate in number,
      pmt_mthd_id in number,
      est_arrival_date in timestamp,
      pickup_order_name in varchar,
      pickup_order_loc in number,
      shipping_address in number,
      track_num in varchar,
      tot out number )
as
begin
    dbms_output.put_line('Finishing online transaction');

    -- Update transaction total

    update transaction
    set
        tax = tax_rate * subtotal,
        total = (1 + tax_rate) * subtotal
    where
        transaction_id = trans_id;
    
    select total into tot 
    from transaction 
    where transaction_id = trans_id;
    
    -- Create online transaction
    
    insert into online_transaction
        (transaction_id, est_arrival)
    values
        (trans_id, est_arrival_date);
    
    -- Create used payment method
    
    insert into used_payment_method
        (transaction_id, payment_method_id, amount)
    values
        (trans_id, pmt_mthd_id, tot);
    
    -- Create shipped order
    
    if shipping_address is not null
    then
        dbms_output.put_line('Creating shipped_order');
        
        insert into shipped_order
            (transaction_id, address_id, tracking_number)
        values
            (trans_id, shipping_address, track_num);
    end if;
    
    -- Create pickup order
    
    if pickup_order_name is not null
    then
        dbms_output.put_line('Creating pickup_order');
        
        insert into pickup_order
            (transaction_id, location_id, pickup_name)
        values
            (trans_id, pickup_order_loc, pickup_order_name);
    end if;
end;

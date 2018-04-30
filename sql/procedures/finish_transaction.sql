create or replace procedure finish_transaction
    ( trans_id in number,
      tax_rate in number,
      pmt_mthd_id in number,
      tot out number )
as
begin
    dbms_output.put_line('Finishing transaction');

    update transaction
    set
        tax = tax_rate * subtotal,
        total = (1 + tax_rate) * subtotal
    where
        transaction_id = trans_id;
    
    select total 
    into tot 
    from transaction 
    where transaction_id = trans_id;
    
    insert into used_payment_method
        (transaction_id, payment_method_id, amount)
    values
        (trans_id, pmt_mthd_id, tot);
    
    -- TODO! support online_transaction, pickup_order, shipped_order,
    --       logging in as customer, selecting payment method, viewing recent transactions
end;

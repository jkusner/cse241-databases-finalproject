create or replace procedure order_inventory
    ( l_id in number,
      p_id in number,
      v_id in number, 
      wanted_shipments in number )
as
    ship_qty number;
    ship_price number;
begin
    dbms_output.put_line('Starting order');

    select shipment_qty, shipment_price
    into ship_qty, ship_price
    from vendor_supply
    where vendor_id = v_id and product_id = p_id;

    ship_qty   := ship_qty   * wanted_shipments;
    ship_price := ship_price * wanted_shipments;

    update stock
    set qty = qty + ship_qty
    where location_id = l_id and product_id = p_id;
    
    dbms_output.put_line('Order complete, got ' || ship_qty || ' for ' || ship_price);
end;

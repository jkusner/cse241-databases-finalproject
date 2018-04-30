create or replace procedure begin_transaction
    ( trans_id out number )
as
    temp_id number;
    cursor findTrans is
        select transaction_id from transaction
        where transaction_id = trans_id;
begin
    trans_id := 0;

    loop
        select trans_id_seq.nextval into trans_id from dual;
        dbms_output.put_line('Next TID: ' || trans_id);
        
        open findTrans;
        fetch findTrans into temp_id;
        exit when findTrans%notfound;
        close findTrans;
    end loop;
    
    close findTrans;
    dbms_output.put_line('Found good TID: ' || trans_id);
    
    insert into transaction
        (transaction_id, subtotal, tax, total)
    values
        (trans_id, 0, 0, 0);
end;

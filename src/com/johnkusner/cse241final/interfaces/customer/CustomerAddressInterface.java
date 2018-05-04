package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Address;
import com.johnkusner.cse241final.objects.Customer;

public class CustomerAddressInterface extends UserInterface {

    private Customer customer;
    private Address address;
    
    public CustomerAddressInterface(Customer cust, Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);
        this.customer = cust;
    }

    @Override
    public void run() {
        try (Statement s = db.createStatement();
                ResultSet rs = s.executeQuery("select * "
                        + "from address natural join customer_address "
                        + "where customer_id = " + customer.getId())) {
            Menu<Address> menu = new Menu<>("Choose an address", this);
            //menu.addItem("Add a new address", null);
            
            while (rs.next()) {
                Address next = new Address(rs);
                menu.addItem(next.toSimpleString(), next);
            }
            
            MenuItem<Address> chosen = menu.display();
            if (chosen == null) {
                return;
            } else if (chosen.get() == null) {
                createAddress();
            } else {
                address = chosen.get();
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void createAddress() {
        // TODO
    }
    
    @Override
    public String getInterfaceName() {
        return "Customer address interface";
    }
    
    public Address getChosenAddress() {
        return address;
    }

}

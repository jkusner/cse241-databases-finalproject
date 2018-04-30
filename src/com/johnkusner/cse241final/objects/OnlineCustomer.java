package com.johnkusner.cse241final.objects;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OnlineCustomer {
    private Customer customer;
    private String username;
    private String passwordHash;
    private Date dateRegistered;
    
    public OnlineCustomer(Customer cust, String username, String passwordHash, Date dateRegistered) {
        this.customer = cust;
        this.username = username;
        this.passwordHash = passwordHash;
        this.dateRegistered = dateRegistered;
    }
    
    public OnlineCustomer(ResultSet rs) throws SQLException {
        this(new Customer(rs), rs.getString("username"), rs.getString("password_hash"), rs.getDate("date_registered"));
    }
    
    public String toString() {
        return username + " (" + this.customer.getFullName() + ")";
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public int getId() {
        return customer.getId();
    }
}

package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private int customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private String company;
    private String email;
    
    public Customer(int customerId, String firstName, String middleName, String lastName, String preferredName, String company, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.company = company;
        this.email = email;
    }
    
    public Customer(ResultSet rs) throws SQLException {
        this(rs.getInt("customer_id"), rs.getString("first_name"), rs.getString("middle_name"),
                rs.getString("last_name"), rs.getString("preferred_name"), rs.getString("company"),
                rs.getString("email"));
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public int getId() {
        return customerId;
    }
}

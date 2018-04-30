package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMethod {
    public int paymentMethodId;
    public String paymentMethodName;
    public int customerId;
    
    public PaymentMethod(int id, String name, int custId) {
        this.paymentMethodId = id;
        this.paymentMethodName = name;
        this.customerId = custId;
    }
    
    public PaymentMethod(ResultSet rs) throws SQLException {
        this(rs.getInt("payment_method_id"), rs.getString("payment_method_name"), rs.getInt("customer_id"));
    }
    
    public String toString() {
        return paymentMethodName;
    }
}

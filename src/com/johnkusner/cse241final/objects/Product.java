package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private int productId;
    private String productName;
    
    public Product(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }
    
    public Product(ResultSet rs) throws SQLException {
        this(rs.getInt("product_id"), rs.getString("product_name"));
    }
    
    public int getId() {
        return productId;
    }
    
    public String getName() {
        return productName;
    }
    
    public String toString() {
        return String.format("%30s", productName);
    }
}

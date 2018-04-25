package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Stock {
	private int productId;
	private String productName;
	private int qty;
	private double unitPrice;

	private NumberFormat numFormat;
	private NumberFormat currencyFormat;

	public Stock(int productId, String productName, int qty, double unitPrice) {
		this.productId = productId;
		this.productName = productName;
		this.qty = qty;
		this.unitPrice = unitPrice;
		
		numFormat = NumberFormat.getNumberInstance();
		currencyFormat = NumberFormat.getCurrencyInstance();
	}
	
	public Stock(ResultSet rs) throws SQLException {
		this(rs.getInt("product_id"), rs.getString("product_name"), rs.getInt("qty"), rs.getDouble("unit_price"));
	}

	public String toString() {
		return String.format("%8d | %20s | %6s | %9s", productId, productName, numFormat.format(qty), currencyFormat.format(unitPrice));
	}
    
    public int getQty() {
        return qty;
    }
	
	public double getUnitPrice() {
	    return unitPrice;
	}

	public static final String HEADER = String.format("%8s | %20s | %6s | %9s", "ID", "Product Name", "QTY", "$/Unit");
}
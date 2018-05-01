package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Inventory {
	private int productId;
	private String productName;
	private int qty;
	private double unitPrice;
	
	private NumberFormat numFormat;
	private NumberFormat currencyFormat;

	public Inventory(int productId, String productName, int qty, double unitPrice) {
		this.productId = productId;
		this.productName = productName;
		this.qty = qty;
		this.unitPrice = unitPrice;

		this.numFormat = NumberFormat.getNumberInstance();
		this.currencyFormat = NumberFormat.getCurrencyInstance();
	}
	
	public Inventory(ResultSet rs) throws SQLException {
		this(rs.getInt("product_id"), rs.getString("product_name"), rs.getInt("qty"), rs.getDouble("unit_price"));
	}

	public int getProductId() {
		return productId;
	}
	
	public int getQty() {
		return qty;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public String toString() {
		return String.format("%30s | %6s | %9s", productName, numFormat.format(qty), currencyFormat.format(unitPrice));
	}

	public static final String HEADER = String.format("%30s | %6s | %9s", "Product Name", "QTY", "Unit Price");
}
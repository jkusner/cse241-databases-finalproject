package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class ProductSale {
	private int productId;
	private String productName;
	private int amountSold;
	private double totalSales;

	private NumberFormat numFormat;
	private NumberFormat currencyFormat;

	public ProductSale(int productId, String productName, int amountSold, double totalSales) {
		this.productId = productId;
		this.productName = productName;
		this.amountSold = amountSold;
		this.totalSales = totalSales;
		
		numFormat = NumberFormat.getNumberInstance();
		currencyFormat = NumberFormat.getCurrencyInstance();
	}
	
	public ProductSale(ResultSet rs) throws SQLException {
		this(rs.getInt("product_id"), rs.getString("product_name"), rs.getInt("amount_sold"), rs.getDouble("total_sales"));
	}

	public String toString() {
		return String.format("%8d | %30s | %6s | %9s", productId, productName, numFormat.format(amountSold), currencyFormat.format(totalSales));
	}

	public static final String HEADER = String.format("%8s | %30s | %6s | %9s", "ID", "Product Name", "QTY", "$ Spent");
}
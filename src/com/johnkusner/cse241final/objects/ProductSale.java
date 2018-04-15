package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductSale {
	private int productId;
	private String productName;
	private int amountSold;
	private double totalSales;

	public ProductSale(int productId, String productName, int amountSold, double totalSales) {
		this.productId = productId;
		this.productName = productName;
		this.amountSold = amountSold;
		this.totalSales = totalSales;
	}
	
	public ProductSale(ResultSet rs) throws SQLException {
		this(rs.getInt("product_id"), rs.getString("product_name"), rs.getInt("amount_sold"), rs.getDouble("total_sales"));
	}

	public String toString() {
		return String.format("%8d | %20s | %6d | $%8.2f", productId, productName, amountSold, totalSales);
	}

	public static final String HEADER = String.format("%8s | %20s | %6s | %8s", "ID", "Product Name", "QTY", "Total Sales");
}
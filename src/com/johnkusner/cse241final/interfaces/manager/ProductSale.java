package com.johnkusner.cse241final.interfaces.manager;

public class ProductSale {
	private int productId;
	private String productName;
	private int amountSold;
	private double totalSales;

	ProductSale(int productId, String productName, int amountSold, double totalSales) {
		this.productId = productId;
		this.productName = productName;
		this.amountSold = amountSold;
		this.totalSales = totalSales;
	}

	public String toString() {
		return String.format("%8d | %20s | %6d | $%8.2f", productId, productName, amountSold, totalSales);
	}

	static final String HEADER = String.format("%8s | %20s | %6s | %8s", "ID", "Product Name", "QTY", "Total Sales");
}
package com.johnkusner.cse241final.objects;

import java.text.NumberFormat;

public class CartItem {
	private Stock stock;
	private int qty;

	private NumberFormat numFormat;
	private NumberFormat currencyFormat;

	public CartItem(Stock stock, int qty) {
		this.stock = stock;
		this.qty = qty;

		numFormat = NumberFormat.getNumberInstance();
		currencyFormat = NumberFormat.getCurrencyInstance();
	}
	
	public String toString() {
		return String.format("%30s | %6s | %9s | %9s", getProductName(), numFormat.format(qty), currencyFormat.format(getUnitPrice()), currencyFormat.format(getTotal()));
	}
    
	public double getUnitPrice() {
		return stock.getUnitPrice();
	}
	
	public double getTotal() {
		return qty * getUnitPrice();
	}
	
    public int getQty() {
        return qty;
    }
    
    public int getMaxQty() {
    	return stock.getQty();
    }

    public String getProductName() {
    	return stock.getProductName();
    }
    
	public boolean setQty(int qty) {
		if (qty <= this.stock.getQty()) {			
			this.qty = qty;
			return true;
		}
		return false;
	}

	public static final String HEADER = String.format("%30s | %6s | %9s | %9s", "Product Name", "QTY", "$/Unit", "Total");
}
package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class SalesTotals {
	private String dateStr;
	private double totalSales;
	private int amountSold;
	private int numTrans;

	private NumberFormat numFormat;
	private NumberFormat currencyFormat;
	
	public SalesTotals(String dateStr, double totalSales, int amountSold, int numTrans) {
		this.dateStr = dateStr;
		this.totalSales = totalSales;
		this.amountSold = amountSold;
		this.numTrans = numTrans;
		
		numFormat = NumberFormat.getNumberInstance();
		currencyFormat = NumberFormat.getCurrencyInstance();
	}
	
	public SalesTotals(ResultSet rs) throws SQLException {
		this(rs.getString("date_str"), rs.getDouble("total_sales"), rs.getInt("amount_sold"), rs.getInt("num_trans"));
	}
	
	public String toString() {
		return String.format("%15s | %10s | %10s | %13s", dateStr, numFormat.format(numTrans), numFormat.format(amountSold), currencyFormat.format(totalSales));
	}
	
	public static final String HEADER = String.format("%15s | %10s | %10s | %13s", "Time Frame", "# Trans", "# Items", "Revenue");
}

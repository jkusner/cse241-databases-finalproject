package com.johnkusner.cse241final.objects;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Transaction {
    private int transactionId;
    private double subtotal;
    private double tax;
    private double total;
    private Date timestamp;
    
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    
    public Transaction(int transactionId, double subtotal, double tax,
            double total, Date timestamp) {
        this.transactionId = transactionId;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.timestamp = timestamp;
        dateFormat = new SimpleDateFormat("MM/d/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm:ss a");
    }

    public Transaction(ResultSet rs) throws SQLException {
        this(rs.getInt("transaction_id"), rs.getDouble("subtotal"),
                rs.getDouble("tax"), rs.getDouble("total"),
                rs.getDate("timestamp"));
    }

    public int getId() {
        return transactionId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return String.format("%8d | %10s | %11s | $%9.2f",
                transactionId, dateFormat.format(timestamp), timeFormat.format(timestamp), total);
    }

    public static final String HEADER = String.format("%8s | %10s | %11s | %10s",
            "ID", "Date", "Time", "Total");

}

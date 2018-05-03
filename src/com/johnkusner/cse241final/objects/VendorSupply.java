package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class VendorSupply {
    private String vendorName;
    private int productId;
    private int vendorId;
    private int shipmentQty;
    private double shipmentPrice;
    
    private NumberFormat numFormat;
    private NumberFormat curFormat;
    
    public VendorSupply(int vendorId, String vendorName, int productId, int shipmentQty, double shipmentPrice) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.productId = productId;
        this.shipmentQty = shipmentQty;
        this.shipmentPrice = shipmentPrice;
        
        this.numFormat = NumberFormat.getNumberInstance();
        this.curFormat = NumberFormat.getCurrencyInstance();
    }
    
    public VendorSupply(ResultSet rs) throws SQLException {
        this(rs.getInt("vendor_id"), rs.getString("vendor_name"),
                rs.getInt("product_id"), rs.getInt("shipment_qty"),
                rs.getDouble("shipment_price"));
    }
    
    public String toString() {
        return String.format("%sx for %s total (%s each) from \"%s\"", 
                numFormat.format(shipmentQty), curFormat.format(shipmentPrice),
                curFormat.format(shipmentPrice / shipmentQty),
                vendorName);
    }
    
    public int getProductId() {
        return productId;
    }
    
    public int getVendorId() {
        return vendorId;
    }
    
    public int getShipmentQty() {
        return shipmentQty;
    }
    
    public double getShipmentPrice() {
        return shipmentPrice;
    }
}

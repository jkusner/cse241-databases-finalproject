package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Stock;
import com.johnkusner.cse241final.objects.Location;
import com.johnkusner.cse241final.objects.VendorSupply;

public class InventoryInterface extends UserInterface {

	private Location loc;
	
	public InventoryInterface(Location loc, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.loc = loc;
	}

	@Override
	public String getInterfaceName() {
		return "Location Stock (" + loc.getName() + ")";
	}
	
	@Override
	public void run() {
		clear();
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM stock natural join product "
						+ "where location_id = " + loc.getId())) {
			
			Menu<Stock> inv = new Menu<Stock>("Stock at \"" + loc.getName() + "\"\n"
			        + "To order more of a product, choose it below.", Stock.HEADER, this);
			while (r.next()) {
				Stock entry = new Stock(r);
				
				inv.addItem(entry);
			}
			
			r.close();
			
			MenuItem<Stock> choice = inv.display();
			if (choice != null && choice.get() != null) {
			    showProductMenu(choice.get());
			    run();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private void showProductMenu(Stock item) {
	    String query = "select * "
	            + "from vendor_supply natural join vendor "
	            + "where product_id = " + item.getProductId() + " "
                + "order by shipment_price / shipment_qty";
	    
	    try (Statement s = db.createStatement();
	            ResultSet rs = s.executeQuery(query)) {
	        String title = "Shipments available for \"" + item.getProductName() + "\"";
	        
	        Menu<VendorSupply> supplyMenu = new Menu<>(title, this);
	        
	        while (rs.next()) {
	            supplyMenu.addItem(new VendorSupply(rs));
	        }
	        
	        MenuItem<VendorSupply> chosen = supplyMenu.display();
	        if (chosen == null || chosen.get() == null) {
	            return;
	        }
	        
	        VendorSupply wanted = chosen.get();
	        
	        int shipments = promptInt("How many shipments would you like to order?", 0, 10);
	        
	        if (shipments <= 0) {
	            return;
	        }
	        
	        purchaseShipments(wanted, shipments, item.getUnitPrice());
	    } catch (Exception e) {
	        handleException(e);
	    }
	}
	
	private void purchaseShipments(VendorSupply wanted, int numShipments, double newPrice) {
	    try (CallableStatement cs = db.prepareCall("{ call order_inventory(?, ?, ?, ?, ?) }");) {

            cs.setInt(1, loc.getId());
            cs.setInt(2, wanted.getProductId());
            cs.setInt(3, wanted.getVendorId());
            cs.setInt(4, numShipments);
            cs.setDouble(5, newPrice);
            
            cs.execute();
            
            cs.close();
            
            pause("Successfully ordered " + numShipments + " shipment"
                    + s(numShipments) + "! Press enter to continue.");
            
	    } catch (Exception e) {
	        out.println("Failed to order inventory.");
	        handleException(e);
	    }
	}

}
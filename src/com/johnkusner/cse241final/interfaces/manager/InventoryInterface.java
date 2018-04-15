package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.objects.Inventory;
import com.johnkusner.cse241final.objects.Location;

public class InventoryInterface extends UserInterface {

	private Location loc;
	
	public InventoryInterface(Location loc, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.loc = loc;
	}

	@Override
	public String getInterfaceName() {
		return "Location Inventory (" + loc.getName() + ")";
	}
	
	@Override
	public void run() {
		clear();
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM stock natural join product "
						+ "where location_id = " + loc.getId())) {
			
			Menu<Inventory> inv = new Menu<Inventory>("Inventory at \"" + loc.getName() + "\"", Inventory.HEADER, this);
			while (r.next()) {
				Inventory entry = new Inventory(r);
				
				inv.addItem(entry);
			}
			
			inv.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
	}

}
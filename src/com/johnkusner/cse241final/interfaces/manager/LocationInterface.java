package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;

public class LocationInterface extends UserInterface {

	private Location loc;
	private Menu<Runnable> menu;
	
	public LocationInterface(Location loc, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.loc = loc;
		
		menu = new Menu<>("Choose an action", this);
		menu.addItem("View Inventory", () -> viewInventory());
		menu.addItem("Recent Transactions", () -> viewTransactions());
	}

	@Override
	public void run() {
		out.println("Viewing Location: \"" + loc.getName() + "\", located at");
		out.println(loc.getAddress().toString());
		
		menu.display();
	}

	private void viewInventory() {
		
	}
	
	private void viewTransactions() {
		
	}
	
	@Override
	public String getInterfaceName() {
		return "Manage Location";
	}

	@Override
	public void close() {
		
	}

}

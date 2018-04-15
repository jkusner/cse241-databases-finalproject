package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
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
		
		menu = new Menu<>("Viewing Location: \"" + loc.getName()
			+ "\", located at\n" + loc.getAddress(), this);
		menu.addItem("View Inventory", () -> viewInventory());
		menu.addItem("Recent Transactions", () -> viewTransactions());
	}

	@Override
	public void run() {
		MenuItem<Runnable> chosen = menu.promptOptional();
		if (chosen != null && chosen.get() != null) {
			chosen.get().run();
		}
	}

	private void viewInventory() {
		new InventoryInterface(loc, in, out, db).run();
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

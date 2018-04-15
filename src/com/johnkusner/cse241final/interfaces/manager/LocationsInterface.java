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

public class LocationsInterface extends UserInterface {

	public LocationsInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
	}

	@Override
	public void run() {
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM location natural join address")) {
			
			Menu<Location> locations = new Menu<Location>("Choose a Location", Location.HEADER, this);
			while (r.next()) {
				locations.addItem(new Location(r));
			}
			
			MenuItem<Location> chosen = locations.display();
			if (chosen != null && chosen.get() != null) {
				viewLocation(chosen.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clear();
	}

	private void viewLocation(Location loc) {
		clear();
		new LocationInterface(loc, in, out, db).run();
	}
	
	@Override
	public String getInterfaceName() {
		return "View/Edit Locations";
	}

	@Override
	public void close() {
		
	}

}

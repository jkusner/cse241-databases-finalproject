package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;

public class ChooseLocationInterface extends UserInterface {

    
    private Location.Type locType;
    private Location location;
    
	public ChooseLocationInterface(Location.Type type, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		locType = type;
	}

	@Override
	public void run() {
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM location natural join address" + locTypeQuery())) {
			
			Menu<Location> locations = new Menu<Location>("Choose a Location", Location.HEADER, this);
			while (r.next()) {
				locations.addItem(new Location(r, locType));
			}

            r.close();
            
			MenuItem<Location> chosen = locations.display();
			if (chosen != null && chosen.get() != null) {
			    clear();
				this.location = chosen.get();
			}
		} catch (Exception e) {
			handleException(e);
		}
		
		clear();
	}

	public Location getLocation() {
	    return location;
	}
	
	@Override
	public String getInterfaceName() {
		return "Choose Location";
	}

	private String locTypeQuery() {
	    if (locType == Location.Type.BOTH) {
	        return "";
	    }
	    return " natural join " + (locType == Location.Type.STORE ? "store" : "warehouse"); 
	}

}

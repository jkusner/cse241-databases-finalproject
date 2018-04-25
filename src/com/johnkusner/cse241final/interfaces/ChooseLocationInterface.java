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

    public static interface LocationChosenCallback {
        void onLocationChosen(Location loc);
    }
    
    private LocationChosenCallback callback;
    
	public ChooseLocationInterface(LocationChosenCallback cb, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.callback = cb;
	}

	@Override
	public void run() {
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM location natural join address")) {
			
			Menu<Location> locations = new Menu<Location>("Choose a Location", Location.HEADER, this);
			while (r.next()) {
				locations.addItem(new Location(r));
			}

            r.close();
            
			MenuItem<Location> chosen = locations.display();
			if (chosen != null && chosen.get() != null) {
			    clear();
				callback.onLocationChosen(chosen.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clear();
	}

	@Override
	public String getInterfaceName() {
		return "Choose Location";
	}

	@Override
	public void close() {
		
	}

}

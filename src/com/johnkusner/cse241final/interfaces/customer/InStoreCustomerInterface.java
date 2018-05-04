package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;

public class InStoreCustomerInterface extends UserInterface {
    
    private Location loc;
    
    public InStoreCustomerInterface(Location loc, Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
        this.loc = loc;
    }

    @Override
    public void run() {
        out.println(this.loc.toString());
        // TODO!
    }

    @Override
    public String getInterfaceName() {
        return "Shop In-Store";
    }

}

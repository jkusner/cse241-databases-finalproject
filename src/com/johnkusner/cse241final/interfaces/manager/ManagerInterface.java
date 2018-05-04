package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ChooseLocationInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.interfaces.customer.ChooseOnlineCustomerInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;

public class ManagerInterface extends UserInterface {

    private Menu<Runnable> menu;

    public ManagerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);

        menu = new Menu<>("Manager Interface", this);
        menu.addItem("View Stores", () -> {
            ChooseLocationInterface choose = new ChooseLocationInterface(Location.Type.STORE, in, out, db);
            choose.run();
            if (choose.getLocation() != null) {
                new ManageLocationInterface(choose.getLocation(), in, out, db).run();                
            }
        });
        menu.addItem("View Warehouses", () -> {
            ChooseLocationInterface choose = new ChooseLocationInterface(Location.Type.WAREHOUSE, in, out, db);
            choose.run();
            if (choose.getLocation() != null) {
                new ManageLocationInterface(choose.getLocation(), in, out, db).run();                
            }
        });
        menu.addItem("View Online Customers", () -> {
            ChooseOnlineCustomerInterface choose = new ChooseOnlineCustomerInterface("Choose customer", in, out, db);
            choose.run();
            if (choose.getChosenCustomer() != null) {
                new ManageCustomerInterface(choose.getChosenCustomer().getCustomer(), in, out, db).run();                
            }
        });
    }

    @Override
    public String getInterfaceName() {
        return "Manager Interface";
    }

    @Override
    public void run() {
        clear();

        MenuItem<Runnable> choice = menu.promptOptional();
        if (choice == null || choice.get() == null) {
            return;
        }

        choice.get().run();

        run();
    }
    
}

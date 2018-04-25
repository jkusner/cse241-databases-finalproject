package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ChooseLocationInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;

public class CustomerInterface extends UserInterface {

    private Menu<UserInterface> menu;
    
    public CustomerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);

        menu = new Menu<>("Choose Customer Interface", this);
        menu.addItem("In-Store Visit", new ChooseLocationInterface(loc -> {
            new InStoreCustomerInterface(loc, in, out, db).run();
        }, in, out, db));
        menu.addItem(new OnlineCustomerInterface(in, out, db));
    }

    @Override
    public void run() {
        clear();
        
        MenuItem<UserInterface> choice = menu.promptOptional();
        if (choice == null || choice.get() == null) {
            return;
        }
        
        choice.get().run();
        
        run();
    }

    @Override
    public String getInterfaceName() {
        return "Customer Interface";
    }

    @Override
    public void close() {
        
    }

}

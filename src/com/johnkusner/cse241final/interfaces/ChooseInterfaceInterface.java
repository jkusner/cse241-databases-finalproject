package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.customer.CustomerInterface;
import com.johnkusner.cse241final.interfaces.manager.ManagerInterface;
import com.johnkusner.cse241final.interfaces.statistics.StatisticsInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;

public class ChooseInterfaceInterface extends UserInterface {

    private Menu<UserInterface> menu;

    public ChooseInterfaceInterface(Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);
    }

    @Override
    public String getInterfaceName() {
        return "Choose interface";
    }

    @Override
    public void run() {
        clear();
        rebuildMenu();
        
        MenuItem<UserInterface> choice = menu.promptOptional();
        if (choice != null && choice.get() != null) {
            choice.get().run();            

            if (promptBool("Would you like to run another interface?")) {
                this.run();
            } else {
                out.println("Goodbye!");
            }
        }
    }

    private void rebuildMenu() {
        menu = new Menu<>("Chose an interface", this);
        menu.addItem(new CustomerInterface(in, out, db));
        menu.addItem(new ManagerInterface(in, out, db));
        menu.addItem(new StatisticsInterface(in, out, db));
    }

}

package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.menu.Menu;

public class ChooseInterfaceInterface extends UserInterface {

    private Menu<UserInterface> menu;

    public ChooseInterfaceInterface(Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);

        menu = new Menu<>("Chose an interface", this);
        menu.addItem(new TestInterface(in, out, db));
     }

    @Override
    public String getInterfaceName() {
        return "Choose interface";
    }

    @Override
    public void run() {
        clear();
        UserInterface choice = menu.prompt().get();
        choice.run();
        
        if (promptBool("Would you like to run another interface?")) {
            this.run();
        }
        else {
            out.println("Goodbye!");
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}

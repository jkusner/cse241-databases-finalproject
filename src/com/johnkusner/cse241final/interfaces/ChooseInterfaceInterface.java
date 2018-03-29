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
        menu.addItem("a", new TestInterface(in, out, db));
        menu.addItem("b", new TestInterface(in, out, db));
        menu.addItem("c", new TestInterface(in, out, db));
        menu.addItem("d", new TestInterface(in, out, db));
        menu.addItem("e", new TestInterface(in, out, db));
        menu.addItem("f", new TestInterface(in, out, db));
        menu.addItem("g", new TestInterface(in, out, db));
        menu.addItem("h", new TestInterface(in, out, db));
        menu.addItem("i", new TestInterface(in, out, db));
        menu.addItem("j", new TestInterface(in, out, db));
        menu.addItem("k", new TestInterface(in, out, db));
        menu.addItem("m", new TestInterface(in, out, db));
        menu.addItem("n", new TestInterface(in, out, db));
        menu.addItem("l", new TestInterface(in, out, db));
        menu.addItem("o", new TestInterface(in, out, db));
    }

    @Override
    public String getInterfaceName() {
        return "Choose interface";
    }

    @Override
    public void run() {
        clear();
        out.println(menu.prompt().getName());
        
        menu.display();
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

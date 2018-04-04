package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.menu.Menu;

public class TestInterface extends UserInterface {

    public TestInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
    }
    
    @Override
    public void run() {
        out.println("~~~ Welcome to interface ~~~\n\n");
        try (Statement s = db.createStatement()) {
            ResultSet r = s.executeQuery("SELECT * FROM PRODUCT");
            
            Menu<String> m = new Menu<String>("Results view", this);
            while (r.next()) {
                m.addItem(r.getString("product_name"));
            }
            m.display();
            
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pause();
    }

    @Override
    public void close() {
        out.println("Closing");
    }

    @Override
    public String getInterfaceName() {
        return "Test Interface";
    }


}

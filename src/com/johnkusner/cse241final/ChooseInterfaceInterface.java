package com.johnkusner.cse241final;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class ChooseInterfaceInterface extends UserInterface {
    
    public ChooseInterfaceInterface(Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);
    }

    @Override
    public String getInterfaceName() {
        return "Choose interface";
    }

    @Override
    public UserInterface run() {
        out.println("Connection worked! :)");
        try (Statement test = db.createStatement();)
        {
            ResultSet result = test.executeQuery("SELECT count(*) from instructor");
            if (!result.next()) {
                out.println("NO DATA! :C");
            } else {
                out.println("Query ran :) > " + result.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

}

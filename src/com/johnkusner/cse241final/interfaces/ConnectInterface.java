package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class ConnectInterface extends UserInterface {
    public ConnectInterface(Scanner in, PrintStream out) {
        super(in, out, null);
    }

    private final String DB_USERNAME = "jjk320";
    private final String CONNECTION_STRING = "jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241";
    
    @Override
    public String getInterfaceName() {
        return "Login";
    }

    @Override
    public void run() {
        out.println("Welcome to Big River Crossing!\n");
        out.println("=== For the best user experience, please make sure this entire sentence fits on one line. ===\n");
        while (true) {
            String password = promptString("Enter password for " + DB_USERNAME);
            if (connect(password)) {
                // Connection was made, work is finished.
                return;
            }
            out.println("\nFailed to connect. Please re-enter password.");
        }
    }
    
    private boolean connect(String password) {
        out.println("Connecting...");

        boolean connected = false;
        
        try (Connection con = DriverManager.getConnection(
                CONNECTION_STRING,
                DB_USERNAME, password)) {
            
            connected = true;
            this.db = con;
            
            ChooseInterfaceInterface choose = new ChooseInterfaceInterface(in, out, this.db);
            choose.run();
        }
        catch (Exception e) {
            if (connected) {
                handleException(e);
            }
        }
        
        return connected;
    }

    @Override
    public void close() {
        
    }

}

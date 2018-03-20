package com.johnkusner.cse241final;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

public abstract class UserInterface extends IOHandler {
    protected Connection db;
    
    public UserInterface() {
        this(new Scanner(System.in), System.out, null);
    }

    public UserInterface(Scanner in, PrintStream out) {
        this(in, out, null);
    }
    
    public UserInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out);
        this.db = db;
    }
    
    public abstract String getInterfaceName();
    public abstract UserInterface run();
    public abstract void close();
}

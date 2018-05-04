package com.johnkusner.cse241final.interfaces;
import java.io.PrintStream;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Scanner;

import com.johnkusner.cse241final.IOHandler;

public abstract class UserInterface extends IOHandler implements Runnable {
    protected Connection db;
    protected NumberFormat numberFormat;
    protected NumberFormat currencyFormat;
    
    public UserInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out);
        this.db = db;
        
        numberFormat = NumberFormat.getNumberInstance();
        currencyFormat = NumberFormat.getCurrencyInstance();
    }
    
    public abstract String getInterfaceName();
    
    public String toString() {
        return getInterfaceName();
    }

    protected String numberFormat(int num) {
        return numberFormat.format(num);
    }
    
    protected String moneyFormat(double num) {
        return currencyFormat.format(num);
    }
    
    protected String s(int val) {
        return val != 1 ? "s" : "";
    }
    
}

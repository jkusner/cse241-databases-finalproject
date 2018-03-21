package com.johnkusner.cse241final;

import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ConnectInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        UserInterface i = new ConnectInterface(scan, System.out);
        i.run();
        
        scan.close();
    }
}

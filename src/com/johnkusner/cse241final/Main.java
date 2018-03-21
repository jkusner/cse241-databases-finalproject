package com.johnkusner.cse241final;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        UserInterface i = new ConnectInterface(scan, System.out);
        i.run();
        
        scan.close();
    }
}

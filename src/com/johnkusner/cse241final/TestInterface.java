package com.johnkusner.cse241final;

import java.io.PrintStream;
import java.util.Scanner;

public class TestInterface extends UserInterface {

    public TestInterface(Scanner in, PrintStream out) {
        super(in, out);
    }
    
    @Override
    public UserInterface run() {
        out.println("~~~ Welcome to interface ~~~\n\n");
        String s = promptString("Give me a string");
        out.println("Thanks for \"" + s + "\"");
        int i = promptInt("Give me an int from 0 to 100", 0, 100);
        out.println("Nice " + i);
        return null;
    }

    @Override
    public void close() {
        out.println("Closing");
    }

    @Override
    public String getInterfaceName() {
        return "Test interface";
    }


}

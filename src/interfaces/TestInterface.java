package interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

public class TestInterface extends UserInterface {

    public TestInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
    }
    
    @Override
    public void run() {
        out.println("~~~ Welcome to interface ~~~\n\n");
        String s = promptString("Give me a string");
        out.println("Thanks for \"" + s + "\"");
        int i = promptInt("Give me an int from 0 to 100", 0, 100);
        out.println("Nice " + i);
        pause();
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

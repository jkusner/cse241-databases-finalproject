package interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChooseInterfaceInterface extends UserInterface {

    private List<UserInterface> choices;

    public ChooseInterfaceInterface(Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);

        choices = new ArrayList<UserInterface>();
        choices.add(new TestInterface(in, out, db));
        choices.add(new TestInterface(in, out, db));
        choices.add(new TestInterface(in, out, db));
    }

    @Override
    public String getInterfaceName() {
        return "Choose interface";
    }

    @Override
    public void run() {
        clear();
        int choice = promptMenu("Welcome, please choose an interface", choices);
        choices.get(choice).run();
        clear();

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

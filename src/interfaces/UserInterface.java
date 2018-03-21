package interfaces;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.IOHandler;

public abstract class UserInterface extends IOHandler {
    protected Connection db;
    
    public UserInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out);
        this.db = db;
    }
    
    public abstract String getInterfaceName();
    public abstract void run();
    public abstract void close();
    
    public String toString() {
        return getInterfaceName();
    }
}

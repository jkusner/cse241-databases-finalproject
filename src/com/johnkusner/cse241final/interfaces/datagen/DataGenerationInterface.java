package com.johnkusner.cse241final.interfaces.datagen;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.johnkusner.cse241final.NamedObject;
import com.johnkusner.cse241final.interfaces.UserInterface;

public class DataGenerationInterface extends UserInterface {
    public DataGenerationInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
    }

    @Override
    public String getInterfaceName() {
        return "Data generation";
    }

    @Override
    public void run() {
        List<NamedObject<Runnable>> choices = new ArrayList<NamedObject<Runnable>>();
        choices.add(new NamedObject<Runnable>("customer", () -> {
            
        }));
    }

    @Override
    public void close() {
        
    }
}

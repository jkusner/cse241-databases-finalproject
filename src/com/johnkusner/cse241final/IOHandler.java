package com.johnkusner.cse241final;
import java.io.PrintStream;
import java.util.Scanner;

public class IOHandler {
    private final String PROMPT_STRING = "> ";
    
    protected Scanner in;
    protected PrintStream out;
    
    public IOHandler(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    public int promptInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(promptString(prompt));
                if (value >= min && value <= max)
                {                    
                    return value;
                }
            }
            catch (Exception e) {
                continue;
            }
            out.printf("Please enter a value between [%d,%d]\n", min, max);
        }
    }
    
    public int promptInt(String prompt) {
        return promptInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
   
    public String promptString(String prompt) {
        out.print(prompt + PROMPT_STRING);
        return in.nextLine();
    }
}

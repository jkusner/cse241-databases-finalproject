package com.johnkusner.cse241final;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class IOHandler {
    private final String PROMPT_STRING = "> ";
    private final int CLEAR_BLANK_LINES = 100;
    
    protected Scanner in;
    protected PrintStream out;
    
    public IOHandler(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    public int promptInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(promptString(prompt).trim());
                if (value >= min && value <= max)
                {                    
                    return value;
                }
            }
            catch (Exception e) {
                // no problem.
            }
            out.printf("Please enter a value between [%d,%d]\n", min, max);
        }
    }
    
    public int promptInt(String prompt) {
        return promptInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public double promptDouble(String prompt, double min, double max) {
        while (true) {
            try {
                double value = Double.parseDouble(promptString(prompt).trim());
                if (value >= min && value <= max)
                {                    
                    return value;
                }
            }
            catch (Exception e) {
                // no problem.
            }
            out.printf("Please enter a value between [%.2f,%.2f]\n", min, max);
        }
    }
    
    public double promptDouble(String prompt) {
        return promptDouble(prompt, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    
    public boolean promptBool(String prompt) {
        while (true) {
            String input = promptString(prompt + " (y/n)").trim();
            if (input.equalsIgnoreCase("y")) {
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            }
            else {
                out.println("Please enter either 'y' or 'n'");
            }
        }
    }
    
    public String promptString(String prompt) {
        out.print(prompt + PROMPT_STRING);
        return in.nextLine();
    }
    
    public void pause() {
        pause("Press enter to continue");
    }
    
    public void pause(String prompt) {
        out.println(prompt);
        in.nextLine();
    }
        
    public void clear() {
        for (int i = 0; i < CLEAR_BLANK_LINES; i++) {
            out.println();
        }
    }
    
    public PrintStream out() {
        return out;
    }
}

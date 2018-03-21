package com.johnkusner.cse241final;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class IOHandler {
    private final String PROMPT_STRING = "> ";
    private final int MENU_PAGE_SIZE = 8;
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
    
    public int promptMenu(String header, List<? extends Object> items) {
        if (items.isEmpty()) {
            return -1;
        } else if (items.size() == 0) {
            return 0;
        }
        
        return promptMenuPage(header, items, 0);
    }
    
    private int promptMenuPage(String header, List<? extends Object> items, int startingItem) {
        clear();
        out.println(header);
        
        int pageSize = Math.min(items.size() - startingItem, MENU_PAGE_SIZE);
        
        boolean showNextPage = items.size() > MENU_PAGE_SIZE + startingItem;
        boolean showPrevPage = startingItem > 0;

        if (showNextPage || showPrevPage) {            
            out.printf("Showing (%d-%d/%d)\n\n", startingItem + 1, startingItem + pageSize, items.size());
        }
        
        int i;
        for (i = 1; i <= pageSize; i++) {
            out.printf("%d) %s\n", i, items.get(startingItem + i - 1));
        }
        
        if (showNextPage || showPrevPage) {
            out.println();
        }
        if (showNextPage) {
            out.printf("%d) Next page\n", i++);    
        }
        if (showPrevPage) {                
            out.printf("%d) Prev page\n", 0);
        }
        
        out.println();
        
        int inputMin = showPrevPage ? 0 : 1;
        int inputMax = i - 1;
        int choice = promptInt(String.format("Select an option [%d-%d]", inputMin, inputMax), inputMin, inputMax);
        
        if (choice >= 1 && choice <= MENU_PAGE_SIZE) {
            clear();
            return startingItem + choice - 1;
        } else {
            if (choice == 0) {
                return promptMenuPage(header, items, startingItem - MENU_PAGE_SIZE);
            } else {
                return promptMenuPage(header, items, startingItem + MENU_PAGE_SIZE);
            }
        }
    }
    
    public void clear() {
        for (int i = 0; i < CLEAR_BLANK_LINES; i++) {
            out.println();
        }
    }
}

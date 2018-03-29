package com.johnkusner.cse241final.menu;

import java.util.ArrayList;
import java.util.List;

import com.johnkusner.cse241final.IOHandler;

public class Menu<T> {

    private final int PAGE_SIZE = 10;
    
    private final String NEXT_PAGE = "n";
    private final String PREV_PAGE = "p";
    private final String DONE = "d";
    
    private IOHandler io;
    private List<MenuItem<T>> items;
    private String prompt;
    
    public Menu(String prompt, IOHandler io) {
        items = new ArrayList<>();
        this.prompt = prompt;
        this.io = io;
    }
    
    public void addItem(MenuItem<T> item) {
        this.items.add(item);
    }
    
    public void addItem(String name, T obj) {
        addItem(new MenuItem<T>(name, obj));
    }
    
    public void addItem(T obj) {
        addItem(new MenuItem<T>(obj));
    }
    
    public MenuItem<T> prompt() {
        if (items.isEmpty()) {
            io.out().println("Data is empty.");
            return null;
        } else if (items.size() == 1) {
            return items.get(0);
        }
        
        return items.get(displayPage(0, true));
    }
    
    public MenuItem<T> display() {
        if (items.isEmpty()) {
            io.out().println("Data is empty");
            return null;
        }
        int result = displayPage(0, false);
        if (result >= 0) {
            return items.get(result);
        }
        return null;
    }
    
    private int displayPage(int startIndex, boolean requireChoice) {
        io.clear();
        io.out().println(prompt);

        int pageSize = Math.min(items.size() - startIndex, PAGE_SIZE);
        
        boolean showNextPage = items.size() > PAGE_SIZE + startIndex;
        boolean showPrevPage = startIndex > 0;
        boolean showDone = !requireChoice;
        
        if (showNextPage || showPrevPage) {            
            io.out().printf("Showing items %d to %d of %d\n\n", startIndex + 1, startIndex + pageSize, items.size());
        }
        
        int i;
        for (i = 0; i < pageSize; i++) {
            io.out().printf("%3d) %s\n", startIndex + i + 1, items.get(startIndex + i).getName());
        }
        i--;
        
        if (showNextPage || showPrevPage) {
            io.out().println();
        }
        if (showNextPage) {
            io.out().printf("%3s) Next page\n", NEXT_PAGE);    
        }
        if (showPrevPage) {                
            io.out().printf("%3s) Prev page\n", PREV_PAGE);
        }
        if (showDone) {
            io.out().printf("%3s) Done", DONE);
        }
        
        io.out().println();
        
        
        while (true) {
            String input = io.promptString("Select an option");
            if (showNextPage && input.equalsIgnoreCase(NEXT_PAGE)) {
                return displayPage(startIndex + PAGE_SIZE, requireChoice);
            } else if (showPrevPage && input.equalsIgnoreCase(PREV_PAGE)) {
                return displayPage(startIndex - PAGE_SIZE, requireChoice);
            } else if (showDone && input.equalsIgnoreCase(DONE)) {
                return -1;
            }
            try {
                int chosen = Integer.parseInt(input);
                if (chosen > 0 && chosen <= items.size()) {
                    return chosen - 1;
                }
            } catch (Exception e) {
                // don't care
            }
            io.out().println("Invalid choice, please try again");
        }
    }    
}

package com.johnkusner.cse241final.menu;

import com.johnkusner.cse241final.IOHandler;

/**
 * Wrapper class for lambda expressions
 */
public class ExecMenu {
    private Menu<Runnable> menu;
    
    public ExecMenu(String prompt, IOHandler io) {
        menu = new Menu<>(prompt, io);
    }
    
    public void addItem(String name, Runnable r) {
        menu.addItem(name, r);
    }
}

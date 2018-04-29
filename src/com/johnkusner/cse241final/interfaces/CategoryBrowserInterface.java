package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Category;

public class CategoryBrowserInterface extends UserInterface {

    private Category parent;
    
    public CategoryBrowserInterface(Category parent, Scanner in, PrintStream out,
            Connection db) {
        super(in, out, db);
        this.parent = parent;
    }
    
    public CategoryBrowserInterface(Scanner in, PrintStream out, Connection db) {
        this(null, in, out, db);
    }

    @Override
    public void run() {
        try (Statement s = db.createStatement();
                ResultSet rs = s.executeQuery(getQuery())) {
            Menu<Category> menu = new Menu<>("Choose a category", this);
            
            while (rs.next()) {
                menu.addItem(new Category(rs));
            }
            
            MenuItem<Category> choice;
            if (parent == null) {
                choice = menu.prompt();
            } else {
                menu.setPrompt("Current category: " + parent);
                choice = menu.promptOptional();
            }
            
            if (choice != null && choice.get() != null) {
                parent = choice.get();
                run();
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    public Category getChosenCategory() {
        return parent;
    }
    
    private String getQuery() {
        return "SELECT * FROM category WHERE parent_id "
                + (parent == null ? "is null" : ("= " + parent.getId()));
    }
    
    @Override
    public String getInterfaceName() {
        return "Category Browser";
    }

    @Override
    public void close() {
        
    }

}

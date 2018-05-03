package com.johnkusner.cse241final.interfaces;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Category;
import com.johnkusner.cse241final.objects.Product;

public class ProductSearchInterface extends UserInterface {

    private Product chosenProduct;
    
    public ProductSearchInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
    }

    @Override
    public void run() {
        clear();
        
        Menu<Runnable> menu = new Menu<>("How would you like to search?", this);
        menu.addItem("Browse by category", () -> browseByCategory());
        menu.addItem("Search for a product by name/id/product", () -> search());
        
        MenuItem<Runnable> chosen = menu.promptOptional();
        
        if (chosen != null && chosen.get() != null) {
            chosen.get().run();

            if (this.chosenProduct == null) {
                if (promptBool("No products matched your search. Search again?")) {
                    run();
                }
            }
        }
    }

    private void browseByCategory() {
        CategoryBrowserInterface catBrowser = new CategoryBrowserInterface(in, out, db);
        catBrowser.run();
        Category cat = catBrowser.getChosenCategory();

        try (Statement s = db.createStatement();
                ResultSet rs = s.executeQuery("select * "
                        + "from product natural join product_category "
                        + "where category_id = " + cat.getId())) {
            showProducts(rs);
        } catch (Exception e) {
            handleException(e);
        }
    }
    
    private void search() {
        String searchTerm = promptSqlSafeString("Type your search term", 3);
        
        String query = "select * " + 
                "from product " + 
                "where lower(product_name) like ? " + 
                "order by lower(product_name) " + 
                "fetch first 100 rows only";
        
        try {
            PreparedStatement s = db.prepareStatement(query);
            
            s.setString(1, "%" + searchTerm + "%");
            
            ResultSet rs = s.executeQuery();
            
            showProducts(rs);
            
            rs.close();
            s.close();
        } catch (Exception e) {
            handleException(e);
        }
    }
    
    private void showProducts(ResultSet rs) throws SQLException {
        Menu<Product> results = new Menu<>("Matched products", this);
        
        int resultsCount = 0;
        
        while (rs.next()) {
            results.addItem(new Product(rs));
            resultsCount++;
        }
        
        if (resultsCount > 0) {
            results.setPrompt("Matching products (" + resultsCount + " total)");
            this.chosenProduct = results.prompt().get();
        } else {
            // No matching product, this is handled in run()
        }
    }
    
    @Override
    public String getInterfaceName() {
        return "Search for a product";
    }

    @Override
    public void close() {
        
    }
    
    public Product getChosenProduct() {
        return this.chosenProduct;
    }

}

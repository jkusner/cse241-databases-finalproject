package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.ProductSale;
import com.johnkusner.cse241final.objects.Stock;

public class OnlineCustomerInterface extends UserInterface {
    
    private NumberFormat currencyFormat;
    private List<Stock> cart;
    
    public OnlineCustomerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
        
        cart = new ArrayList<>();
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void run() {
        clear();
        
        showMenu();
    }

    private void showMenu() {
        Menu<Runnable> menu = new Menu<>("What would you like to do?", this);
        menu.addItem("Search for a product", () -> productSearch());
        if (!cart.isEmpty()) {
            menu.addItem("View/Edit cart", () -> editCart());
            menu.addItem("Checkout (Total: " + moneyFormat(totalCartPrice()) + ")", () -> checkout());            
        }
        menu.addItem("Cancel", () -> cancel());
        
        if (!cart.isEmpty()) {
        }
    }
    
    private void productSearch() {
        
    }
    
    private void editCart() {
        
    }
    
    private void cancel() {
        
    }
    
    private void checkout() {
        
    }
    
    private double totalCartPrice() {
        return cart.stream().mapToDouble(s -> s.getUnitPrice() * s.getQty()).sum();
    }
    
    private String moneyFormat(double num) {
        return currencyFormat.format(num);
    }
    
    @Override
    public String getInterfaceName() {
        return "Online Customer Interface";
    }

    @Override
    public void close() {
        
    }

}

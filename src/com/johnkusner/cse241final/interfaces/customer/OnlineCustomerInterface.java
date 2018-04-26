package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ProductSearchInterface;
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
        String cartStatus = "Your cart is empty.";
        
        Menu<Runnable> menu = new Menu<>("What would you like to do?", this);
        menu.addItem("Search for a product", () -> productSearch());
        if (!cart.isEmpty()) {
            menu.addItem("View/Edit cart", () -> editCart());
            menu.addItem("Checkout (Total: " + moneyFormat(totalCartPrice()) + ")", () -> checkout()); 
            cartStatus = "You have " + cart.size() + " items in your cart.";
        }
        menu.addItem("Cancel", () -> {});
        
        menu.setPrompt(cartStatus + "\nWhat would you like to do?");
        menu.prompt().get().run();
        
        showMenu();
    }
    
    private void productSearch() {
        new ProductSearchInterface(in, out, db).run();
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

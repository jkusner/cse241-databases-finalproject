package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ProductSearchInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.CartItem;
import com.johnkusner.cse241final.objects.Product;
import com.johnkusner.cse241final.objects.Stock;

public class OnlineCustomerInterface extends UserInterface {

    private NumberFormat numberFormat;
    private NumberFormat currencyFormat;
    private List<CartItem> cart;
    
    public OnlineCustomerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
        
        cart = new ArrayList<>();
        
        numberFormat = NumberFormat.getNumberInstance();
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
        menu.addItem("Add a product to cart", () -> productSearch());
        if (!cart.isEmpty()) {
            menu.addItem("View/Edit cart", () -> editCart());
            menu.addItem("Checkout", () -> checkout()); 
            
            cartStatus = getCartStatusMessage();
        }
        
        menu.setPrompt(cartStatus + " What would you like to do?");
        MenuItem<Runnable> choice = menu.promptOptional();
        
        if (choice != null && choice.get() != null) {
            choice.get().run();
        } else {
            return;
        }
        
        showMenu();
    }
    
    private void productSearch() {
        ProductSearchInterface search = new ProductSearchInterface(in, out, db);
        search.run();
        
        Product chosen = search.getChosenProduct();
        
        if (chosen != null) {
            showAvailability(chosen);
        }
    }
    
    private void editCart() {
        String title = getCartStatusMessage();
        title += "\nSelect an item below to modify it.";
        
    	Menu<CartItem> cartDisplay = new Menu<>(title, CartItem.HEADER, this);
    	
        for (CartItem item : cart) {
        	cartDisplay.addItem(item);
        }
        
        MenuItem<CartItem> chosen = cartDisplay.promptOptional();
        
        if (chosen != null && chosen.get() != null) {
        	editItem(chosen.get());
        }
    }
    
    private void editItem(CartItem item) {
    	clear();
    	out.println("Your cart contains " + numberFormat(item.getQty()) + "x \"" + item.getProductName() + "\"");
    	int newQty = promptInt("Enter new quantity (0 to remove)", 0, item.getMaxQty());
    	if (newQty == 0) {
    		cart.remove(item);
    	} else {
    		item.setQty(newQty);
    	}
    }
    
    private void checkout() {
        
    }
    
    private void showAvailability(Product prod) {
        try (Statement s = db.createStatement();
                ResultSet rs = s.executeQuery("select * from warehouse_stock where product_id = " + prod.getId())) {
            clear();
            if (!rs.next()) {
                out.println("Sorry, " + prod.getName() + " is out of stock.");
                pause();
            } else {
                Stock stock = new Stock(rs);
                out.println("Availability for " + prod.getName() + ": ");
                out.println(stock.toSimpleString(false));
                out.println();
                int wanted = promptInt("How many would you like to add to cart?", 0, stock.getQty());
                
                if (wanted == 0) {
                    return;
                }
                
                cart.add(new CartItem(stock, wanted));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getCartStatusMessage() {
    	int items = totalCartItems();
    	return "You have " + cart.size() + " product" + s(cart.size()) + " (" + numberFormat(items) + " item" + s(items)
    			+ ") in your cart. Sub-total: " + moneyFormat(totalCartPrice()) + ".";
    }

    private double totalCartPrice() {
        return cart.stream().mapToDouble(i -> i.getTotal()).sum();
    }
    
    private int totalCartItems() {
        return cart.stream().mapToInt(i -> i.getQty()).sum();
    }

    private String numberFormat(int num) {
        return numberFormat.format(num);
    }
    
    private String moneyFormat(double num) {
        return currencyFormat.format(num);
    }
    
    private String s(int val) {
    	return val != 1 ? "s" : "";
    }
    
    @Override
    public String getInterfaceName() {
        return "Shop On-line";
    }

    @Override
    public void close() {
        
    }

}

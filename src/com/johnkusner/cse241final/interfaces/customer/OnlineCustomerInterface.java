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
import com.johnkusner.cse241final.objects.Product;
import com.johnkusner.cse241final.objects.ProductSale;
import com.johnkusner.cse241final.objects.Stock;

public class OnlineCustomerInterface extends UserInterface {

    private NumberFormat numberFormat;
    private NumberFormat currencyFormat;
    private List<Stock> cart;
    
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
            menu.addItem("Checkout (Total: " + moneyFormat(totalCartPrice()) + ")", () -> checkout()); 
            
            cartStatus = "You have " + cart.size() + " product(s) (" + numberFormat(totalCartItems()) + " item(s)) in your cart.";
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
                
                stock.setQty(wanted);
                cart.add(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double totalCartPrice() {
        return cart.stream().mapToDouble(s -> s.getUnitPrice() * s.getQty()).sum();
    }
    
    private int totalCartItems() {
        return cart.stream().mapToInt(s -> s.getQty()).sum();
    }

    private String numberFormat(int num) {
        return numberFormat.format(num);
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

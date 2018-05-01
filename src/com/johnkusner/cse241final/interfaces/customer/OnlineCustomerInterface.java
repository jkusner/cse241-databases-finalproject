package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ProductSearchInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Address;
import com.johnkusner.cse241final.objects.CartItem;
import com.johnkusner.cse241final.objects.OnlineCustomer;
import com.johnkusner.cse241final.objects.PaymentMethod;
import com.johnkusner.cse241final.objects.Product;
import com.johnkusner.cse241final.objects.Stock;

public class OnlineCustomerInterface extends UserInterface {

    private OnlineCustomer customer;
    private PaymentMethod paymentMethod;
    
    private NumberFormat numberFormat;
    private NumberFormat currencyFormat;
    private List<CartItem> cart;
    
    private Address shipTo;
    
    private boolean finished = false;
    
    public OnlineCustomerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
        
        cart = new ArrayList<>();
        
        numberFormat = NumberFormat.getNumberInstance();
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void run() {
        clear();
        
        ChooseOnlineCustomerInterface custLoginInterface = new ChooseOnlineCustomerInterface(in, out, db);
        custLoginInterface.run();
        
        customer = custLoginInterface.getChosenCustomer();
        
        if (customer == null) {
            return;
        }
        
        paymentMethod = custLoginInterface.choosePaymentMethod();
        
        if (paymentMethod == null) {
            pause("No payment method chosen, press any key to exit interface.");
            return;
        }
        
        CustomerAddressInterface addressInterface = new CustomerAddressInterface(customer.getCustomer(), in, out, db);
        addressInterface.run();
        
        shipTo = addressInterface.getChosenAddress();
        
        if (shipTo == null) {
            pause("No address has been chosen, press any key to exit interface.");
            return;
        }
        
        showMenu();
    }

    private void showMenu() {
        String cartStatus = "Your cart is empty.";
        
        Menu<Runnable> menu = new Menu<>("Choose an option", this);
        menu.addItem("Add a product to cart", () -> productSearch());
        if (!cart.isEmpty()) {
            menu.addItem("View/Edit cart", () -> editCart());
            menu.addItem("Checkout", () -> checkout()); 
            
            cartStatus = getCartStatusMessage();
        }
        
        menu.setPrompt("Hello, " + customer.getCustomer().getFullName() + "!\n" 
                + cartStatus + " What would you like to do?");
        MenuItem<Runnable> choice = menu.promptOptional();
        
        if (choice != null && choice.get() != null) {
            choice.get().run();
        } else {
            return;
        }
        
        if (!finished) {            
            showMenu();
        }
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
        if (!promptBool("Are you sure you would like to checkout?")) {
        	return;
        }
        clear();
        out.println("Processing transaction...");
        
        try {
        	db.setAutoCommit(false);
        	
        	CallableStatement cs = db.prepareCall("{ call begin_transaction(?) }");
        	
        	cs.registerOutParameter(1, Types.INTEGER);
        	
        	cs.execute();
        	
        	int transactionId = cs.getInt(1);
        	
        	clear();
        	out.println("Transaction ID: " + transactionId);
        	
        	cs.close();
        	
        	
        	cs = db.prepareCall("{ call purchase_product(?, ?, ?, ?, ?, ?) }");;
        	
        	int totalItemsPurchased = 0;
        	double totalMoneySpent = 0.00;

        	for (CartItem item : cart) {
        		cs.setInt(1, transactionId);
        		cs.setInt(2, item.getProductId());
        		cs.setInt(3, item.getQty());
        		cs.setDouble(4, item.getUnitPrice());
        		cs.registerOutParameter(5, Types.INTEGER); // amount_got
        		cs.registerOutParameter(6, Types.DOUBLE); // total_paid
        		
        		cs.execute();
        	
        		int purchasedQty = cs.getInt(5);
        		double purchasePrice = cs.getDouble(6);
        		
        		totalItemsPurchased += purchasedQty;
        		totalMoneySpent += purchasePrice;
        		
        		if (purchasedQty > 0) {
                    String stockNotice = "";
                    if (purchasedQty < item.getQty()) {
                        stockNotice = " (no more stock in price range)";
                    }
                    
                    out.printf("Got %sx \"%s\" for %s total%s\n", numberFormat(purchasedQty),
                            item.getProductName(), moneyFormat(purchasePrice), stockNotice);
        		} else {
        		    out.printf("Out of stock: %s\n", item.getProductName());
        		}
        	}
        	
        	cs.close();
            
        	if (totalItemsPurchased == 0) {
            	db.rollback();
            	
            	finished = true;
            	out.println("We are sorry, it looks like every item in your cart has sold out.");
            	pause("Press any key to exit interface");
            	return;
        	} else {
        	    // 2-day shipping
        	    Date estArrival = new Date(new java.util.Date().getTime() + (2 * 24 * 60 * 60 * 1000));
        	    
        	    cs = db.prepareCall("{ call finish_online_transaction(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
        	    
        	    cs.setInt(1, transactionId);
        	    cs.setDouble(2, 0.0); // tax rate
        	    cs.setDouble(3, paymentMethod.getId());
        	    cs.setDate(4, estArrival);
//        	    if (pickupOrder) {
//        	        cs.setString(5, "pickup order name"); // TODO!
//        	        cs.setInt(6, 1); // pickup order location TODO!
//        	        cs.setNull(7, Types.INTEGER); // shipping address
//        	        cs.setNull(8, Types.VARCHAR); // tracking number
//        	    } else {
                    cs.setNull(5, Types.VARCHAR);
                    cs.setNull(6, Types.INTEGER);
                    cs.setInt(7, shipTo.getId());
                    cs.setString(8, "tracking number"); // TODO!
//        	    }
        	    
        	    cs.registerOutParameter(9, Types.DOUBLE); // final total
        	    
        	    cs.execute();
        	    
        	    totalMoneySpent = cs.getDouble(9);
        	    
        	    cs.close();
        	    
        		db.commit();
        		
        		// Committed successfully
        		finished = true;        		
        		out.println("\nTransaction complete!");
        		out.printf("Total items purchased: %s\nCharged %s to %s\n",
        		        numberFormat(totalItemsPurchased),
        		        moneyFormat(totalMoneySpent),
        		        paymentMethod.toString());
        		out.printf("Your order will arrive soon at %s!\n", shipTo.toSimpleString());
        		pause("\nPress enter to exit interface.");
        		return;
        	}
        } catch (Exception e) {
            try {
                db.rollback();
            } catch (Exception e2) {
                // ignore
                e2.printStackTrace();
            }
            
            e.printStackTrace();
        } finally {
            try {
                db.setAutoCommit(true);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        out.println("We are sorry, something went wrong and we were unable to complete your order.");
        finished = !promptBool("Would you like to try again?");
    }
    
    private void showAvailability(Product prod) {
    	// If this product is already in cart, editItem instead
    	CartItem item = cart.stream().filter(i -> i.getProductId() == prod.getId()).findFirst().orElse(null);
    	if (item != null) {
    		editItem(item);
    		return;
    	}
        try (Statement stmt = db.createStatement();
                ResultSet rs = stmt.executeQuery("select * from warehouse_stock where product_id = " + prod.getId())) {
            clear();
            if (!rs.next()) {
                out.println("Sorry, " + prod.getName() + " is out of stock.");
                pause();
            } else {
                out.println("Availability for \"" + prod.getName() + "\": ");

                List<Stock> available = new ArrayList<Stock>();
                int totalAvailable = 0;
                
                do {
                    Stock stock = new Stock(rs);
                    available.add(stock);
                    totalAvailable += stock.getQty();

                    if (available.size() <= 5) {
                        out.println("- " + stock.toSimpleString(false));                        
                    }
                } while (rs.next());
                
                double averageCost = available.stream().mapToDouble(st -> st.getUnitPrice()).average().orElse(0);
                averageCost = Math.ceil(averageCost * 100) / 100.0;
                
                out.println("Cheaper items sell first. Your guaranteed price is <= "
                        + moneyFormat(averageCost) + "/each");
                int wanted = promptInt("Enter desired quantity (0 to cancel)", 0, totalAvailable);
                
                if (wanted == 0) {
                    return;
                }
                
                cart.add(new CartItem(new Stock(prod.getId(), prod.getName(), totalAvailable, averageCost), wanted));
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

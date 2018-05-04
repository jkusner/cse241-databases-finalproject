package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.ChooseLocationInterface;
import com.johnkusner.cse241final.interfaces.ProductSearchInterface;
import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Address;
import com.johnkusner.cse241final.objects.CartItem;
import com.johnkusner.cse241final.objects.Location;
import com.johnkusner.cse241final.objects.OnlineCustomer;
import com.johnkusner.cse241final.objects.PaymentMethod;
import com.johnkusner.cse241final.objects.Product;
import com.johnkusner.cse241final.objects.Stock;

public class CustomerInterface extends UserInterface {

    public static enum Type {
        PICKUP_ORDER("In-store Pickup"),
        SHIPPED_ORDER("Shipped Order");
        
        private String name;
        
        Type(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    }
    
    private OnlineCustomer customer;
    private PaymentMethod paymentMethod;
    
    private List<CartItem> cart;
    
    private Type orderType;
    private Address shipTo;
    private Location pickupLocation;
    private String pickupName;
    
    private boolean finished = false;
    
    public CustomerInterface(Scanner in, PrintStream out, Connection db) {
        super(in, out, db);
        
        cart = new ArrayList<>();
    }

    @Override
    public void run() {
        clear();
        
        ChooseOnlineCustomerInterface custLoginInterface = new ChooseOnlineCustomerInterface(
                "Welcome to BRC Customer Interface! Choose a user to login as...", in, out, db);
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
        
        Menu<Type> typeMenu = new Menu<>("Choose order type", this);
        typeMenu.addItem(Type.SHIPPED_ORDER);
        typeMenu.addItem(Type.PICKUP_ORDER);
        
        MenuItem<Type> chosenType = typeMenu.promptOptional();
        if (chosenType == null || chosenType.get() == null) {
            return;
        }
        
        orderType = chosenType.get();
        
        if (orderType == Type.SHIPPED_ORDER) {
            CustomerAddressInterface addressInterface = new CustomerAddressInterface(customer.getCustomer(), in, out, db);
            addressInterface.run();
            
            shipTo = addressInterface.getChosenAddress();
            
            if (shipTo == null) {
                pause("No address has been chosen, press enter to exit interface.");
                return;
            }
        } else {
            ChooseLocationInterface locationInterface = new ChooseLocationInterface(Location.Type.STORE, in, out, db);
            locationInterface.run();
            pickupLocation = locationInterface.getLocation();
            
            if (pickupLocation == null) {
                pause("No address has been chosen, press enter to exit interface.");
                return;
            }
            
            pickupName = promptSqlSafeString("Who will pick up this order (type a name)?", 2);
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
        
        String inventoryType = orderType == Type.PICKUP_ORDER
                ? ("You are shopping for pickup at " + pickupLocation.getName() + ".")
                : "You are shopping online inventory.";
        
        menu.setPrompt("Hello, " + customer.getCustomer().getFullName() + "! " + inventoryType + "\n" 
                + cartStatus + " What would you like to do?");
        MenuItem<Runnable> choice = menu.promptOptional();
        
        if (choice != null && choice.get() != null) {
            choice.get().run();
        } else {
            if (promptBool("Are you sure you want to log out of customer interface?")) {
                return;                
            }
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
            editCart();
        }
    }
    
    private void editItem(CartItem item) {
    	clear();
    	out.println("Your cart contains " + numberFormat(item.getQty()) + "x \"" + item.getProductName() + "\"");
    	out.println("There are " + numberFormat(item.getMaxQty()) + " available.");
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
        	out.println("Transaction ID: " + transactionId + "\n");
        	
        	cs.close();
        	
        	
        	cs = db.prepareCall("{ call purchase_product(?, ?, ?, ?, ?, ?, ?) }");;
        	
        	int totalItemsPurchased = 0;
        	double totalMoneySpent = 0.00;

        	for (CartItem item : cart) {
        		cs.setInt(1, transactionId);
        		if (orderType == Type.SHIPPED_ORDER) {
        		    cs.setNull(2, Types.INTEGER);
        		} else {
        		    cs.setInt(2, pickupLocation.getId());
        		}
        		cs.setInt(3, item.getProductId());
        		cs.setInt(4, item.getQty());
        		cs.setDouble(5, item.getUnitPrice());
        		cs.registerOutParameter(6, Types.INTEGER); // amount_got
        		cs.registerOutParameter(7, Types.DOUBLE); // total_paid
        		
        		cs.execute();
        	
        		int purchasedQty = cs.getInt(6);
        		double purchasePrice = cs.getDouble(7);
        		
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
            
        	out.println();
        	
        	if (totalItemsPurchased == 0) {
            	db.rollback();
            	
            	finished = true;
            	out.println("We are sorry, it looks like every item in your cart has sold out.");
            	pause("Press any key to exit interface");
            	return;
        	} else {
        	    long shippingTime = orderType == Type.PICKUP_ORDER
        	            ? (2 * 60 * 60 * 1000) // 2 hour pickup
	                    : (2 * 24 * 60 * 60 * 1000); // 2 day shipping
        	    Date estArrival = new Date(new java.util.Date().getTime() + shippingTime);
        	    
        	    cs = db.prepareCall("{ call finish_online_transaction(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
        	    
        	    cs.setInt(1, transactionId);
        	    cs.setDouble(2, 0.0); // tax rate
        	    cs.setDouble(3, paymentMethod.getId());
        	    cs.setDate(4, estArrival);
        	    if (orderType == Type.PICKUP_ORDER) {
        	        cs.setString(5, pickupName);
        	        cs.setInt(6, pickupLocation.getId());
        	        cs.setNull(7, Types.INTEGER); // shipping address
        	        cs.setNull(8, Types.VARCHAR); // tracking number
        	    } else {
                    cs.setNull(5, Types.VARCHAR); // pickup name
                    cs.setNull(6, Types.INTEGER); // pickup location
                    cs.setInt(7, shipTo.getId());
                    cs.setString(8, getRandomTrackingNumber());
        	    }
        	    
        	    cs.registerOutParameter(9, Types.DOUBLE); // final total
        	    
        	    cs.execute();
        	    
        	    totalMoneySpent = cs.getDouble(9);
        	    
        	    cs.close();
        	    
        		db.commit();
        		
        		// Committed successfully
        		finished = true;        		
        		out.printf("Thank you for shopping with BRC, %s!\n", customer.getCustomer().getFullName());
        		out.printf("Total items purchased: %s\nCharged %s to %s\n",
        		        numberFormat(totalItemsPurchased),
        		        moneyFormat(totalMoneySpent),
        		        paymentMethod.toString());
        		
        		String destination = orderType == Type.SHIPPED_ORDER
        		        ? shipTo.toSimpleString()
		                : pickupLocation.getName();
        		        
        		out.printf("Your order will arrive soon at %s!\n", destination);
        		pause("\nPress enter to log out of interface.");
        		return;
        	}
        } catch (Exception e) {
            try {
                db.rollback();
            } catch (Exception e2) {
                // ignore
            }
        } finally {
            try {
                db.setAutoCommit(true);
            }
            catch (Exception e) {
                // ignore
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
    	
    	String query;
    	
    	if (orderType == Type.SHIPPED_ORDER) {
    	    query = "select * from warehouse_stock where product_id = "
                    + prod.getId() + " and qty > 0";
    	} else {
    	    query = "select * from stock "
    	            + "natural join product where "
    	            + "product_id = " + prod.getId() + " and "
                    + "location_id = " + pickupLocation.getId() + " and "
                    + "qty > 0";
    	}
    	
        try (Statement stmt = db.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
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
                
                double averageCost = available.stream().mapToDouble(st -> st.getQty() * st.getUnitPrice()).sum();
                averageCost /= totalAvailable;
                averageCost = Math.ceil(averageCost * 100) / 100.0;
                
                if (available.size() > 1) {
                    out.println("Cheaper items sell first. Your guaranteed price is <= "
                            + moneyFormat(averageCost) + "/each");
                }
                
                int wanted = promptInt("Enter desired quantity (0 to cancel)", 0, totalAvailable);
                
                if (wanted == 0) {
                    return;
                }
                
                cart.add(new CartItem(new Stock(prod.getId(), prod.getName(), totalAvailable, averageCost), wanted));
            }
        } catch (Exception e) {
            handleException(e);
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

    @Override
    public String getInterfaceName() {
        return "Customer Interface";
    }
    
    private String getRandomTrackingNumber() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 15; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

}

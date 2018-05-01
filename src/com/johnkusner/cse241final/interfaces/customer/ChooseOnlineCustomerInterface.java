package com.johnkusner.cse241final.interfaces.customer;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;
import com.johnkusner.cse241final.objects.OnlineCustomer;
import com.johnkusner.cse241final.objects.PaymentMethod;

public class ChooseOnlineCustomerInterface extends UserInterface {

    public static interface LocationChosenCallback {
        void onLocationChosen(Location loc);
    }
    
    private OnlineCustomer chosen;
    private String prompt;
    
	public ChooseOnlineCustomerInterface(String prompt, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.prompt = prompt;
	}
	
	public ChooseOnlineCustomerInterface(Scanner in, PrintStream out, Connection db) {
	    this("Log in as...", in, out, db);
	}

	@Override
	public void run() {
		try (Statement s = db.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM online_customer natural join customer "
				        + "order by lower(username)")) {
			
			Menu<OnlineCustomer> customers = new Menu<>(prompt, this);
			while (rs.next()) {
				customers.addItem(new OnlineCustomer(rs));
			}

            rs.close();
            
			MenuItem<OnlineCustomer> chosen = customers.promptOptional();
			if (chosen != null && chosen.get() != null) {
			    this.chosen = chosen.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clear();
	}

	@Override
	public String getInterfaceName() {
		return "Choose Location";
	}

	@Override
	public void close() {
		
	}

	public PaymentMethod choosePaymentMethod() {
        try (Statement s = db.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM payment_method "
                        + "where customer_id = " + chosen.getId() + " "
                        + "order by lower(payment_method_name)")) {
            
            Menu<PaymentMethod> methods = new Menu<>("Welcome back, " + chosen.getCustomer().getFullName() + ". Please choose your payment method.", this);
            while (rs.next()) {
                methods.addItem(new PaymentMethod(rs));
            }

            rs.close();
            
            MenuItem<PaymentMethod> chosen = methods.display();
            if (chosen != null && chosen.get() != null) {
                return chosen.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
	}
	
    public OnlineCustomer getChosenCustomer() {
        return this.chosen;
    }

}

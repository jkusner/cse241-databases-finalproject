package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Customer;

public class ManageCustomerInterface extends UserInterface {

	private Customer customer;
	private Menu<Runnable> menu;
	
	public ManageCustomerInterface(Customer cust, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.customer = cust;
		
		menu = new Menu<>("Viewing Customer: \"" + customer.getFullName()
			+ "\", ID: " + customer.getId(), this);
		menu.addItem("Recent transactions", () -> showRecentTransactions());
	}

	@Override
	public void run() {
		MenuItem<Runnable> chosen = menu.promptOptional();
		if (chosen != null && chosen.get() != null) {
			chosen.get().run();
			run();
		}
	}

	public void showRecentTransactions() {
	    new RecentTransactionsInterface(in, out, db).showForCustomer(customer);
	}
	
	@Override
	public String getInterfaceName() {
		return "Manage Customer";
	}

}

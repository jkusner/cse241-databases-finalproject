package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Location;
import com.johnkusner.cse241final.objects.Transaction;

public class RecentTransactionsInterface extends UserInterface {

	private Location loc;
	
	public RecentTransactionsInterface(Location loc, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.loc = loc;
	}

	@Override
	public String getInterfaceName() {
		return "Recent Transactions at (" + loc.getName() + ")";
	}
	
	@Override
	public void run() {
		clear();
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM transaction natural join physical_transaction "
						+ "where location_id = " + loc.getId() + " "
						+ "order by timestamp desc "
				        + "fetch first 100 rows only")) {
			
			Menu<Transaction> trans = new Menu<Transaction>("Recent Transactions at \"" + loc.getName() + "\"\nSelect a transaction to see more info.", Transaction.HEADER, this);
			while (r.next()) {
				Transaction t = new Transaction(r);
				
				trans.addItem(t);
			}
			
			r.close();
			
			MenuItem<Transaction> chosen = trans.display();
			if (chosen != null && chosen.get() != null) {
				Transaction t = chosen.get();
				new TransactionDetailInterface(t, in, out, db).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
	}

}
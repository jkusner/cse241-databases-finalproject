package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.Customer;
import com.johnkusner.cse241final.objects.Location;
import com.johnkusner.cse241final.objects.Transaction;

public class RecentTransactionsInterface extends UserInterface {
	
	public RecentTransactionsInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
	}

	@Override
	public String getInterfaceName() {
		return "Recent Transactions";
	}
	
	@Override
	public void run() {
	}

    public void showForStore(Location loc) {
        clear();
        show("SELECT * FROM transaction natural join physical_transaction "
                    + "where location_id = " + loc.getId() + " "
                    + "order by timestamp desc "
                    + "fetch first 100 rows only",
                    "Recent Transactions at \"" + loc.getName() + "\"");
    }
    
    public void showForCustomer(Customer cust) {
        clear();
        show("select * " + 
                "from used_payment_method inner join transaction using (transaction_id) " + 
                "where payment_method_id in ( " + 
                "    select payment_method_id " + 
                "    from payment_method " + 
                "    where customer_id = " + cust.getId() + " " + 
                ")",
                "Recent Transactions from \"" + cust.getFullName() + "\" (Customer ID#" + cust.getId() + ")");
    }
	
	private void show(String query, String title) {
	    try (Statement s = db.createStatement();
                ResultSet r = s.executeQuery(query)) {
            Menu<Transaction> trans = new Menu<Transaction>(title + "\nSelect a transaction to see more info.", Transaction.HEADER, this);
            boolean foundAny = false;
            
            while (r.next()) {
                foundAny = true;
                
                Transaction t = new Transaction(r);
                
                trans.addItem(t);
            }
            
            r.close();
            
            if (!foundAny) {
                pause("No transactions were found. Press enter to continue.");
                return;
            }
            
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
package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.objects.ProductSale;
import com.johnkusner.cse241final.objects.Transaction;

public class TransactionDetailInterface extends UserInterface {

	private Transaction trans;
	
	public TransactionDetailInterface(Transaction trans, Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		this.trans = trans;
	}

	@Override
	public String getInterfaceName() {
		return "Transaction Detail for (" + trans.getId() + ")";
	}
	
	@Override
	public void run() {
		clear();
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("select product_id, product_name, unit_price * qty as total_sales, qty as amount_sold "
						+ "from transaction natural join purchased natural join product "
						+ "where transaction_id = " + trans.getId())) {
			
			Menu<ProductSale> soldStuff = new Menu<ProductSale>("Products Purchased in Transaction "
			        + trans.getId() + " on " + trans.getFormattedTimestamp(), ProductSale.HEADER, this);
			while (r.next()) {
				ProductSale sale = new ProductSale(r);
				
				soldStuff.addItem(sale);
			}
			
			r.close();
			
			soldStuff.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
	}

}
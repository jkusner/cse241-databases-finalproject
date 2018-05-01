package com.johnkusner.cse241final.interfaces.statistics;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;
import com.johnkusner.cse241final.objects.ProductSale;

public class StatisticsInterface extends UserInterface {

	private Menu<Runnable> menu;
	
	public StatisticsInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		
		menu = new Menu<>("Which statistics would you like to view?", this);
		menu.addItem("Top sellers", () -> showTopSellers());
	}

	@Override
	public String getInterfaceName() {
		return "Statistics Interface";
	}
	
	@Override
	public void run() {
		clear();
		MenuItem<Runnable> choice = menu.promptOptional();
		if (choice == null) {
			return;
		}
		
		choice.get().run();
	}
	
	public void showTopSellers() {
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT * FROM top_selling_products")) {
			
			Menu<ProductSale> sales = new Menu<ProductSale>("Top sellers", ProductSale.HEADER, this);
			while (r.next()) {
				ProductSale sale = new ProductSale(r);
				
				sales.addItem(sale);
			}
			
			r.close();
			
			sales.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		clear();
	}
	
	@Override
	public void close() {
	}

}

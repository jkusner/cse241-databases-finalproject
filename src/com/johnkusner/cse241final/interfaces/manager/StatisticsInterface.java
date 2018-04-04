package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.ArrayList;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;

public class StatisticsInterface extends UserInterface {

	private Menu<Runnable> menu;
	
	public StatisticsInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		
		menu = new Menu<>("Which statistics would you like to view?", this);
		menu.addItem("Test", () -> { out.println("hello"); });
		menu.addItem("Top sellers", () -> showTopSellers());
	}

	@Override
	public String getInterfaceName() {
		return "View Statistics";
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
		try (Statement s = db.createStatement()) {
			ResultSet r = s.executeQuery("SELECT * FROM top_selling_products");
			
			Menu<ProductSale> sales = new Menu<ProductSale>("Top sellers", ProductSale.HEADER, this);
			while (r.next()) {
				ProductSale sale = new ProductSale(r.getInt("product_id"),
						r.getString("product_name"), r.getInt("amount_sold"),
						r.getDouble("total_sales"));
				
				sales.addItem(sale);
			}
			
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

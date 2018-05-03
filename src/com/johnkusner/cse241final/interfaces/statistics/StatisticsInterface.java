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
import com.johnkusner.cse241final.objects.SalesTotals;

public class StatisticsInterface extends UserInterface {

	static enum TimeFrame {
		ALL_TIME("all time"),
		YEAR("year"),
		QUARTER("quarter"),
		MONTH("month"),
		WEEK("week"),
		DAY("day");
		
		private String name;
		
		TimeFrame(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
	}
	
	private Menu<Runnable> menu;
	
	public StatisticsInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		
		menu = new Menu<>("Which statistics would you like to view?", this);
		menu.addItem("Top product sales...", () -> showTopSellers());
		menu.addItem("Sales totals...", () -> showSalesTotals());
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
		
		run();
	}
	
	public void showTopSellers() {
		Menu<TimeFrame> timeFrameMenu = new Menu<>("Top Sellers - Choose time frame", this);
		timeFrameMenu.addItem("All time", TimeFrame.ALL_TIME);
		timeFrameMenu.addItem("Past year", TimeFrame.YEAR);
		timeFrameMenu.addItem("Past quarter", TimeFrame.QUARTER);
		timeFrameMenu.addItem("Past month", TimeFrame.MONTH);
		timeFrameMenu.addItem("Past week", TimeFrame.WEEK);
		timeFrameMenu.addItem("Past day", TimeFrame.DAY);
		
		MenuItem<TimeFrame> frameChoice = timeFrameMenu.promptOptional();
		if (frameChoice == null) {
			return;
		}

		TimeFrame timeFrame = frameChoice.get();
		
		String query;
		
		switch (timeFrame) {
		case YEAR:
			query = "SELECT * FROM top_selling_products_year";
			break;
		case QUARTER:
			query = "SELECT * FROM top_selling_products_quarter";
			break;
		case MONTH:
			query = "SELECT * FROM top_selling_products_month";
			break;
		case WEEK:
			query = "SELECT * FROM top_selling_products_day";
			break;
		case DAY:
			query = "SELECT * FROM top_selling_products_day";
			break;
		default:
			query = "SELECT * FROM top_selling_products";
			break;
		}
		
		
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery(query)) {
			
			Menu<ProductSale> sales = new Menu<>("Top sellers (" + timeFrame + ")", ProductSale.HEADER, this);
			while (r.next()) {
				ProductSale sale = new ProductSale(r);
				
				sales.addItem(sale);
			}
			
			r.close();
			
			sales.display();
		} catch (Exception e) {
			handleException(e);
		}
		
		clear();
		
		showTopSellers();
	}
	
	public void showSalesTotals() {
		Menu<TimeFrame> timeFrameMenu = new Menu<>("Sales totals - Choose time frame", this);
		timeFrameMenu.addItem("Per day", TimeFrame.DAY);
		timeFrameMenu.addItem("Per week", TimeFrame.WEEK);
		timeFrameMenu.addItem("Per month", TimeFrame.MONTH);
		timeFrameMenu.addItem("Per quarter", TimeFrame.QUARTER);
		
		MenuItem<TimeFrame> frameChoice = timeFrameMenu.promptOptional();
		if (frameChoice == null) {
			return;
		}

		TimeFrame timeFrame = frameChoice.get();
		
		String query;
		
		switch (timeFrame) {
		case DAY:
			query = "SELECT * FROM sales_totals_per_day";
			break;
		case WEEK:
			query = "SELECT * FROM sales_totals_per_week";
			break;
		case MONTH:
			query = "SELECT * FROM sales_totals_per_month";
			break;
		default:
			query = "SELECT * FROM sales_totals_per_quarter";
			break;
		}
		
		
		try (Statement s = db.createStatement();
				ResultSet r = s.executeQuery(query)) {
			
			Menu<SalesTotals> sales = new Menu<>("Sales totals (per " + timeFrame + ")", SalesTotals.HEADER, this);
			while (r.next()) {
				SalesTotals sale = new SalesTotals(r);
				
				sales.addItem(sale);
			}
			
			r.close();
			
			sales.display();
		} catch (Exception e) {
			handleException(e);
		}
		
		clear();
		
		showSalesTotals();
	}
	
	@Override
	public void close() {
	}

}

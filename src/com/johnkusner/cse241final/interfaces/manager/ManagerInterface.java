package com.johnkusner.cse241final.interfaces.manager;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.Scanner;

import com.johnkusner.cse241final.interfaces.UserInterface;
import com.johnkusner.cse241final.menu.Menu;
import com.johnkusner.cse241final.menu.MenuItem;

public class ManagerInterface extends UserInterface {

	private Menu<UserInterface> menu;
	
	public ManagerInterface(Scanner in, PrintStream out, Connection db) {
		super(in, out, db);
		
		menu = new Menu<>("Manager Interface", this);
		menu.addItem(new StatisticsInterface(in, out, db));
		menu.addItem(new LocationsInterface(in, out, db));
	}

	@Override
	public String getInterfaceName() {
		return "Manager Interface";
	}
	
	@Override
	public void run() {
		clear();
		
		MenuItem<UserInterface> choice = menu.promptOptional();
		if (choice == null || choice.get() == null) {
			return;
		}
		
		choice.get().run();
		
		run();
	}

	@Override
	public void close() {
		
	}
}

package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Location {
	private int locationId;
	private String locationName;
	private Address address;

	public Location(int locationId, String locationName, Address address) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.address = address;
	}

	public Location(ResultSet rs) throws SQLException {
		this(rs.getInt("location_id"), rs.getString("location_name"), new Address(rs));
	}

	public int getId() {
		return locationId;
	}
	
	public String getName() {
		return locationName;
	}
	
	public String toString() {
		return String.format("%20s", locationName);
	}
	
	public Address getAddress() {
		return address;
	}

	public static final String HEADER = String.format("%20s", "Loc. Name");
}
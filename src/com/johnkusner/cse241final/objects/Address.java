package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Address {
	private int addressId;
	private String line1;
	private String line2;
	private String line3;
	private String city;
	private String state;
	private String zip;
	private boolean active;

	public Address(int addressId, String line1, String line2, String line3, String city, String state, String zip,
			boolean active) {
		this.addressId = addressId;
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.active = active;
	}

	public Address(ResultSet rs) throws SQLException {
		this(rs.getInt("address_id"), rs.getString("line1"), rs.getString("line2"), rs.getString("line3"),
				rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getBoolean("active"));
	}

	public int getId() {
	    return addressId;
	}
	
	public boolean isActive() {
	    return active;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(line1).append('\n');
		if (line2 != null && !line2.isEmpty()) {
			sb.append(line2).append('\n');
		}
		if (line3 != null && !line3.isEmpty()) {
			sb.append(line3).append('\n');
		}
		sb.append(city).append(", ").append(state);
		sb.append(' ').append(zip).append('\n');
		return sb.toString();
	}
	
	public String toSimpleString() {
	    return line1 + nextPart(line2) + nextPart(line3) + nextPart(city) + nextPart(state) + " " + zip;
	}
	
	private String nextPart(String thing) {
	    if (thing != null) {
	        return ", " + thing;
	    }
	    return "";
	}

}

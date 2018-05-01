package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Location {

    public static enum Type {
        STORE,
        WAREHOUSE,
        BOTH
    }
    
	private int locationId;
	private String locationName;
	private Address address;
	private Type locType;

	public Location(int locationId, String locationName, Address address, Type locType) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.address = address;
		this.locType = locType;
	}

	public Location(ResultSet rs, Type locType) throws SQLException {
        this(rs.getInt("location_id"), rs.getString("location_name"), new Address(rs), locType);
    }
	
	public Location(ResultSet rs) throws SQLException {
        this(rs.getInt("location_id"), rs.getString("location_name"), new Address(rs), Type.BOTH);
    }
	
	public int getId() {
		return locationId;
	}
	
	public String getName() {
		return locationName;
	}
	
	public Type getType() {
	    return locType;
	}
	
	public String toString() {
		return String.format("%20s", locationName);
	}
	
	public Address getAddress() {
		return address;
	}

	public static final String HEADER = String.format("%20s", "Loc. Name");
}
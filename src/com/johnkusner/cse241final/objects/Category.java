package com.johnkusner.cse241final.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private int categoryId;
    private String categoryName;
    private int parentId;
    
    public Category(int categoryId, String categoryName, int parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
    }
    
    public Category(ResultSet rs) throws SQLException {
        this(rs.getInt("category_id"), rs.getString("category_name"), rs.getInt("parent_id"));
    }
    
    public int getId() {
        return categoryId;
    }
    
    public String getName() {
        return categoryName;
    }
    
    public int getParentId() {
        return parentId;
    }
    
    public String toString() {
        return categoryName;
    }
}

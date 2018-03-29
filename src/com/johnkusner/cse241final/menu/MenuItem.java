package com.johnkusner.cse241final.menu;

public class MenuItem<T> {
    private String name;
    private T obj;
    public MenuItem(String name, T obj) {
        this.name = name;
        this.obj = obj;
    }
    public MenuItem(T obj) {
        this(obj.toString(), obj);
    }
    public String getName() {
        return name;
    }
    public T get() {
        return obj;
    }
}

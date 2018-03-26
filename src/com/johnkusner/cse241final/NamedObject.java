package com.johnkusner.cse241final;

public class NamedObject<T> {
    private String name;
    private T object;
    public NamedObject(String name, T object) {
        this.name = name;
        this.object = object;
    }
    public T get() {
        return object;
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }
}

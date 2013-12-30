package org.osito.shoppinglist.domain;

public class Item {

    private long id;
    private String name;

    public Item(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}

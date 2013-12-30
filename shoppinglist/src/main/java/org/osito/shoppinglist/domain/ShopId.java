package org.osito.shoppinglist.domain;

public class ShopId {

    private Long id;
    private String name;

    public ShopId(Long id, String name) {
        this.id = id;
        this.name = name.trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

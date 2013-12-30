package org.osito.shoppinglist.presentation.shoppinglist;

import org.osito.shoppinglist.domain.Item;

import java.util.List;

public interface ItemsView {
    void setItems(List<Item> items);

    void appendItems(List<Item> items);
}

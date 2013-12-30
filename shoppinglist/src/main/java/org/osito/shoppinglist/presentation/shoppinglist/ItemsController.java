package org.osito.shoppinglist.presentation.shoppinglist;

import org.osito.androidpromise.deferred.Task;
import org.osito.shoppinglist.application.ShoppingListService;
import org.osito.shoppinglist.domain.Item;

import java.util.List;

public class ItemsController {

    private ShoppingListService shoppingListService;
    private long shopId;

    public ItemsController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    public void init(long shopId, final ItemsView view) {
        this.shopId = shopId;
        shoppingListService.getItems(shopId)
                           .thenOnMainThread(new Task<List<Item>>() {
                               @Override
                               public void run(List<Item> items) {
                                   view.setItems(items);
                               }
                           });
    }

    public void removeItem(Item item) {
        shoppingListService.remove(item);
    }

    public void addItems(String items, final ItemsView view) {
        shoppingListService.persistItems(shopId, items.split(","))
                           .thenOnMainThread(new Task<List<Item>>() {
                               @Override
                               public void run(List<Item> items) {
                                   view.appendItems(items);
                               }
                           });
    }
}

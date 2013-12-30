package org.osito.shoppinglist.presentation.shops;

import android.content.Context;
import android.content.Intent;

import org.osito.androidpromise.deferred.Task;
import org.osito.shoppinglist.domain.ShopId;
import org.osito.shoppinglist.application.ShoppingListService;
import org.osito.shoppinglist.presentation.shoppinglist.ItemsActivity;

import java.util.List;

public class ShopsController {

    private ShoppingListService shoppingListService;

    public ShopsController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    public void loadShoppingLists(final ShopsView view) {
        shoppingListService.getShops()
                           .thenOnMainThread(new Task<List<ShopId>>() {
                               @Override
                               public void run(List<ShopId> shopIds) {
                                   view.setShoppingLists(shopIds);
                               }
                           });
    }

    public void removeShop(ShopId id) {
        shoppingListService.removeShop(id);
    }

    public void addShops(String shops, final ShopsView view) {
        shoppingListService.persistShops(shops.split(","))
                           .thenOnMainThread(new Task<List<ShopId>>() {
                               @Override
                               public void run(List<ShopId> shopIds) {
                                   view.appendShops(shopIds);
                               }
                           });
    }

    public void openShoppingList(ShopId shopId, Context context) {
        Intent intent = new Intent(context, ItemsActivity.class);
        intent.putExtra("shop_id", shopId.getId());
        context.startActivity(intent);
    }
}

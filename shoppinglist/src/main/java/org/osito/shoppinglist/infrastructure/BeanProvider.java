package org.osito.shoppinglist.infrastructure;

import android.content.Context;

import org.osito.shoppinglist.application.ShoppingListService;
import org.osito.shoppinglist.presentation.shoppinglist.ItemsController;
import org.osito.shoppinglist.presentation.shops.ShopsController;

public class BeanProvider {

    public static ShoppingListService shoppingListService(Context context) {
        return new ShoppingListService(shopDbHelper(context));
    }

    public static ShopsController shopsController(Context context) {
        return new ShopsController(shoppingListService(context));
    }

    public static ItemsController shoppingListController(Context context) {
        return new ItemsController(shoppingListService(context));
    }

    public static ShopDbHelper shopDbHelper(Context context) {
        return new ShopDbHelper(context);
    }

}

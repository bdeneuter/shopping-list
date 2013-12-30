package org.osito.shoppinglist.application;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.osito.androidpromise.deferred.Promise;
import org.osito.shoppinglist.domain.Item;
import org.osito.shoppinglist.domain.ShopId;
import org.osito.shoppinglist.infrastructure.DatabaseContract;
import org.osito.shoppinglist.infrastructure.ShopDbHelper;

import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.collect.Lists.newArrayList;
import static org.osito.androidpromise.deferred.Function.execute;

public class ShoppingListService {

    private ShopDbHelper shopDbHelper;

    public ShoppingListService(ShopDbHelper shopDbHelper) {
        this.shopDbHelper = shopDbHelper;
    }

    public Promise<List<ShopId>> getShops() {
        return execute(new Callable<List<ShopId>>() {
            @Override
            public List<ShopId> call() throws Exception {
                List<ShopId> result = newArrayList();
                SQLiteDatabase database = shopDbHelper.getReadableDatabase();
                try {
                    Cursor cursor = database.query(DatabaseContract.Shop.TABLE_NAME, null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        result.add(new ShopId(cursor.getLong(cursor.getColumnIndex(DatabaseContract.Shop._ID)),
                                                      cursor.getString(cursor.getColumnIndex(DatabaseContract.Shop.COLUMN_NAME))));
                    }
                } finally {
                    database.close();
                }


                return result;
            }
        });
    }

    public Promise<List<Item>> getItems(final long shopId) {
        return execute(new Callable<List<Item>>() {
            @Override
            public List<Item> call() throws Exception {
                List<Item> result = newArrayList();
                SQLiteDatabase database = shopDbHelper.getReadableDatabase();
                try {
                    Cursor cursor = database.query(DatabaseContract.Item.TABLE_NAME, null, DatabaseContract.Item.COLUMN_NAME_SHOP_ID + " LIKE ?", new String[]{Long.toString(shopId)}, null, null, null);
                    while(cursor.moveToNext()) {
                        result.add(new Item(cursor.getLong(cursor.getColumnIndex(DatabaseContract.Item._ID)), cursor.getString(cursor.getColumnIndex(DatabaseContract.Item.COLUMN_NAME))));
                    }
                } finally {
                    database.close();
                }
                return result;
            }
        });
    }

    public void removeShop(final ShopId id) {
        execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                SQLiteDatabase database = shopDbHelper.getWritableDatabase();
                try {
                    database.delete(DatabaseContract.Shop.TABLE_NAME, "_id = ?", new String[]{Long.toString(id.getId())});
                } finally {
                    database.close();
                }
                return null;
            }
        });
    }

    public Promise<List<ShopId>> persistShops(final String... shopNames) {
        return execute(new Callable<List<ShopId>>() {
            @Override
            public List<ShopId> call() throws Exception {
                List<ShopId> result = newArrayList();
                for(String shopName : shopNames) {
                    result.add(persistShop(shopName));
                }
                return result;
            }
        });
    }

    private ShopId persistShop(String shopName) {
        SQLiteDatabase database = shopDbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Shop.COLUMN_NAME, shopName);
            long id = database.insert(DatabaseContract.Shop.TABLE_NAME, null, values);
            return new ShopId(id, shopName);
        } finally {
            database.close();
        }
    }

    public Promise<List<Item>> persistItems(final long shop, final String... items) {
        return execute(new Callable<List<Item>>() {
            @Override
            public List<Item> call() throws Exception {
                List<Item> result = newArrayList();
                for(String item : items) {
                    result.add(persistItem(shop, item));
                }
                return result;
            }
        });
    }

    private Item persistItem(long shop, String item) {
        SQLiteDatabase database = shopDbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Item.COLUMN_NAME, item);
            values.put(DatabaseContract.Item.COLUMN_NAME_SHOP_ID, shop);
            long id = database.insert(DatabaseContract.Item.TABLE_NAME, null, values);
            return new Item(id, item);
        } finally {
            database.close();
        }
    }

    public void remove(final Item item) {
        execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                SQLiteDatabase database = shopDbHelper.getWritableDatabase();
                try {
                    database.delete(DatabaseContract.Item.TABLE_NAME, "_id = ?", new String[]{Long.toString(item.getId())});
                } finally {
                    database.close();
                }
                return null;
            }
        });
    }

}

package org.osito.shoppinglist.infrastructure;

import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_SHOPS =
            "CREATE TABLE " + Shop.TABLE_NAME + " (" +
                    Shop._ID + " INTEGER PRIMARY KEY," +
                    Shop.COLUMN_NAME + TEXT_TYPE + " )";
    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + Item.TABLE_NAME + " (" +
                    Item._ID + " INTEGER PRIMARY KEY," +
                    Item.COLUMN_NAME_SHOP_ID + " REFERENCES " + Shop.TABLE_NAME + " ON DELETE CASCADE," +
                    Item.COLUMN_NAME + TEXT_TYPE + " )";

    public static final String SQL_DELETE_SHOPS =
            "DROP TABLE IF EXISTS " + Shop.TABLE_NAME;
    public static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + Item.TABLE_NAME;

    public static abstract class Shop implements BaseColumns {
        public static final String TABLE_NAME = "shop";
        public static final String COLUMN_NAME = "name";
    }

    public static abstract class Item implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_SHOP_ID = "shop_id";
        public static final String COLUMN_NAME = "name";
    }

}

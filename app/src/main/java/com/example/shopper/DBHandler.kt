package com.example.shopper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DBHandler
/**
 * Create a version of the shopper database
 * @param context reference to the activity that initializes a DBHandler
 * @param factory null
 */
(context: Context?, factory: CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    /**
     * Create Shopper database tables
     * @param sqLiteDatabase reference to shopper database
     */
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        // define create statement for shoppinglist table and store it
        // in String
        val query = "CREATE TABLE " + TABLE_SHOPPING_LIST + "(" +
                COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LIST_NAME + " TEXT, " +
                COLUMN_LIST_STORE + " TEXT, " +
                COLUMN_LIST_DATE + " TEXT);"

        // execute the statement
        sqLiteDatabase.execSQL(query)

        // define create statement for shoppinglistitem table and store it
        // in String
        val query2 = "CREATE TABLE " + TABLE_SHOPPING_LIST_ITEM + "(" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_PRICE + " DECIMAL(10,2), " +
                COLUMN_ITEM_QUANTITY + " INTEGER, " +
                COLUMN_ITEM_HAS + " TEXT, " +
                COLUMN_ITEM_LIST_ID + " INTEGER);"

        // execute the statement
        sqLiteDatabase.execSQL(query2)
    }

    /**
     * Create a new version of the shopper database.
     * @param sqLiteDatabase reference to shopper database
     * @param oldVersion old version of the Shopper database
     * @param newVersion new version of the Shopper database
     */
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // define drop statement and store it in a String
        val query = "DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST"

        // execute the drop statement
        sqLiteDatabase.execSQL(query)

        // define drop statement and store it in a String
        val query2 = "DROP TABLE IF EXISTS $TABLE_SHOPPING_LIST"

        // execute the drop statement
        sqLiteDatabase.execSQL(query2)

        // call method that creates the tables
        onCreate(sqLiteDatabase)
    }

    /**
     * This method gets called when the add button in the Action bar of the
     * CreateList Activity gets clicked. It inserts a new raw into the shopping
     * list table.
     * @param name shopping list name
     * @param store shopping list store
     * @param date shopping list date
     */
    fun addShoppingList(name: String?, store: String?, date: String?) {

        // get reference to the shopper database
        val db = writableDatabase
        // initialize a ContentValues object
        val values = ContentValues()

        // put data into ContentValues object
        values.put(COLUMN_LIST_NAME, name)
        values.put(COLUMN_LIST_STORE, store)
        values.put(COLUMN_LIST_DATE, date)

        // insert data in ContentValues object into shoppinglist table
        db.insert(TABLE_SHOPPING_LIST, null, values)

        // close database reference
        db.close()
    }

    /**
     * This method gets called when the add button in the Action bar of the
     * AddItem Activity gets clicked. It inserts a new raw into the shopping
     * listitem table.
     * @param name item name
     * @param price item price
     * @param quantity item quantity
     * @param listId
     */
    fun addItemToList(name: String?, price: Double?, quantity: Int?, listId: Int?) {

        // get reference to the shopper database
        val db = writableDatabase
        // initialize a ContentValues object
        val values = ContentValues()

        // put data into ContentValues object
        values.put(COLUMN_ITEM_NAME, name)
        values.put(COLUMN_ITEM_PRICE, price)
        values.put(COLUMN_ITEM_QUANTITY, quantity)
        values.put(COLUMN_ITEM_HAS, "false")
        values.put(COLUMN_ITEM_LIST_ID, listId)

        // insert data in ContentValues object into shoppinglistitem table
        db.insert(TABLE_SHOPPING_LIST_ITEM, null, values)

        // close database reference
        db.close()
    }// get reference to the shopper database

    // define select statement and store it in a String

    // execute select statement and return it as a Cursor

    /**
     * This method gets called when the MAinActivity is created. It will
     * select and return all of the data in the shoppinglist table.
     * @return Cursor that contains all data in the shoppinglist table.
     */
    val shoppingLists: Cursor
        get() {

            // get reference to the shopper database
            val db = writableDatabase

            // define select statement and store it in a String
            val query = "SELECT * FROM $TABLE_SHOPPING_LIST"

            // execute select statement and return it as a Cursor
            return db.rawQuery(query, null)
        }

    /**
     * This method gets called when an item in the ListView is started
     * @param id shopping list id
     * @return shopping list name
     */
    fun getShoppingListName(id: Int): String {

        // get reference to the shopper database
        val db = writableDatabase

        // declare and initialize the String that will be returned
        var name = ""

        // define select statement
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + COLUMN_LIST_ID + " = " + id

        // execute select statement and store it in a Cursor
        val cursor = db.rawQuery(query, null)

        // move ro the first row in the Cursor
        cursor.moveToFirst()

        // check that name component of Cursor isn't equal to null
        if (cursor.getString(cursor.getColumnIndex("name")) != null) {
            // get the name component of the Cursor and store it in a String
            name = cursor.getString(cursor.getColumnIndex("name"))
        }

        // close reference to shopper database
        db.close()

        // return shopping list name
        return name
    }

    /**
     * This method gets called when the ViewList Activity is clicked
     * @param listId shopping list id
     * @return Cursor that contains all of the items associated with
     * the specified shopping list id
     */
    fun getShoppingListItems(listId: Int): Cursor {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statement and store it in a String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute select statement and return it as a Cursor
        return db.rawQuery(query, null)
    }

    /**
     * This method gets called when an item in the ViewList activity is clicked
     * @param itemId database id of the clicked item
     * @return 1 if the clicked item is unpurchased, else 0
     */
    fun isItemUnPurchased(itemId: Int): Int {

        // get reference to the shopper database
        val db = writableDatabase

        // define select statement and store it in a String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_HAS + " = \"false\" " +
                " AND " + COLUMN_ITEM_ID + " = " + itemId

        // execute select statement and store result in a Cursor
        val cursor = db.rawQuery(query, null)

        // return a count of the number of rows in the Cursor
        return cursor.count
    }

    /**
     * This method gets called when an item in the ViewList activity is clicked
     * It sets the clicked item's item_has value to true.
     * @param itemId database id of the clicked item
     */
    fun updateItem(itemId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // define update statement and store it in a String
        val query = "UPDATE " + TABLE_SHOPPING_LIST_ITEM + " SET " +
                COLUMN_ITEM_HAS + " = \"true\" " + "WHERE " +
                COLUMN_ITEM_ID + " = " + itemId

        // execute the update statement
        db.execSQL(query)

        // close db connection
        db.close()
    }

    /**
     * This method is going to get called when the viewItem activity is started.
     * @param itemId database id of clicked shopping list item
     * @return Cursor that contain all
     */
    fun getShoppingListItem(itemId: Int): Cursor {

        // get reference to the shopper database
        val db = writableDatabase
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_ID + " = " + itemId

        // execute  statement and return it as a Cursor
        return db.rawQuery(query, null)
    }

    /**
     * This method gets cold when the delete button in the Action bar of
     * @param itemId
     */
    fun deleteShoppingListItem(itemId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // delete statement and store it in String
        val query = "DELETE FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_ID + " = " + itemId

        // execute the delete statement
        db.execSQL(query)

        // close database reference
        db.close()
    }

    /**
     * This method gets cold when the delete button in the Action bar of the
     * view List activity gets clicked. It delete a row in the shoppinglistitem
     * and shoppinglist table
     * @param listId database id of the shopping list to be deleted
     */
    fun deleteShoppingList(listId: Int) {

        // get reference to the shopper database
        val db = writableDatabase

        // delete statement and store it in String
        val query1 = "DELETE FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId

        // execute the delete statement
        db.execSQL(query1)

        // delete statement and store it in String
        val query2 = "DELETE FROM " + TABLE_SHOPPING_LIST +
                " WHERE " + COLUMN_LIST_ID + " = " + listId

        // execute the delete statement
        db.execSQL(query2)

        // close database reference
        db.close()
    }

    /**
     *
     * @param listId
     * @return
     */
    fun getShoppingListTotalCost(listId: Int): String {

        // get reference to the shopper database
        val db = writableDatabase

        // declare and initialize the String returned by the method
        var cost = ""

        // declare a Double
        var totalCost = 0.0

        // define select statement and store it in String
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_LIST_ID + " = " + listId
        val c = db.rawQuery(query, null)

        // Loop through the row in the Cursor
        while (c.moveToNext()) {
            totalCost += c.getDouble(c.getColumnIndex("price")) *
                    c.getInt(c.getColumnIndex("quantity"))
        }
        cost = totalCost.toString()
        db.close()

        // return String
        return cost
    }

    /**
     * This method gets cold when a shopping list item is clicked in the ViewList activity.
     * @param listid database id of the shopping list on wich the shopping list
     * @return
     */
    fun getUnpurchasedItems(listId: Int): Int {

        // get reference to the shopper database
        val db = writableDatabase
        val query = "SELECT * FROM " + TABLE_SHOPPING_LIST_ITEM +
                " WHERE " + COLUMN_ITEM_HAS + " = \"false\" " +
                " AND " + COLUMN_ITEM_LIST_ID + " = " + listId
        val cursor = db.rawQuery(query, null)
        return cursor.count
    }

    companion object {
        // initialize constants for the DB ma,e and version
        const val DATABASE_NAME = "shopper.db"
        const val DATABASE_VERSION = 2

        // initialize constants for the shoppinglist table
        const val TABLE_SHOPPING_LIST = "shoppinglist"
        const val COLUMN_LIST_ID = "_id"
        const val COLUMN_LIST_NAME = "name"
        const val COLUMN_LIST_STORE = "store"
        const val COLUMN_LIST_DATE = "date"

        // initialize constants for the shoppinglistitem table
        const val TABLE_SHOPPING_LIST_ITEM = "shoppinglistitem"
        const val COLUMN_ITEM_ID = "_id"
        const val COLUMN_ITEM_NAME = "name"
        const val COLUMN_ITEM_PRICE = "price"
        const val COLUMN_ITEM_QUANTITY = "quantity"
        const val COLUMN_ITEM_HAS = "item_has"
        const val COLUMN_ITEM_LIST_ID = "list_id"
    }
}
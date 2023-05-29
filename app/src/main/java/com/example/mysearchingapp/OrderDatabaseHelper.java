package com.example.mysearchingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.example.mysearchingapp.util.Util;

import java.util.ArrayList;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    public OrderDatabaseHelper(@Nullable Context context) {
        super(context, Util.ORDER_DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ORDER_TABLE = "CREATE TABLE "
                + Util.ORDER_TABLE_NAME
                + " ("
                + Util.ORDER_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.USERNAME + " TEXT , "
                + Util.FLOWER_IMAGE + " BLOB , "
                + Util.RECEIVER_NAME + " TEXT , "
                + Util.DATE + " TEXT , "
                + Util.TIME + " TEXT , "
                + Util.DESTINATION+ " TEXT , "
                + Util.DESTINATION_LATITUDE + " REAL , "
                + Util.DESTINATION_LONGITUDE + " REAL , "
                + Util.FLOWER_TYPE + " TEXT , "
                + Util.QUANTITY + " TEXT , "
                + Util.MSG + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
    }

    //Update username
    public void updateUsername(String oldUsername, ContentValues contentValues)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update(Util.ORDER_TABLE_NAME, contentValues, Util.USERNAME + "=?", new String[] {oldUsername});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_ORDER_TABLE, new String[]{Util.ORDER_TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    //Get order count for user
    public int getOrderCount(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.ORDER_TABLE_NAME + " WHERE " + Util.USERNAME + "=?", new String[] {username});

        Integer count = cursor.getCount();
        return count;
    }

    //Insert order into database
    public long insertOrder(Order order) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERNAME, order.getUsername());
        contentValues.put(Util.FLOWER_IMAGE, order.getFlowerImageBytes());
        contentValues.put(Util.RECEIVER_NAME, order.getReceiverName());
        contentValues.put(Util.DATE, order.getDate());
        contentValues.put(Util.TIME, order.getTime());
        contentValues.put(Util.DESTINATION, order.getDestination());
        contentValues.put(Util.DESTINATION_LATITUDE, order.getDestinationLatitude());
        contentValues.put(Util.DESTINATION_LONGITUDE, order.getDestinationLongitude());
        contentValues.put(Util.FLOWER_TYPE, order.getFlowerType());
        contentValues.put(Util.QUANTITY, order.getQuantity());
        contentValues.put(Util.MSG, order.getMessage());

        long rowId = sqLiteDatabase.insert(Util.ORDER_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return rowId;
    }

    //Get all orders in the database
    @SuppressLint("Range")
    public ArrayList<Order> fetchAllOrders(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.ORDER_TABLE_NAME + " WHERE " + Util.USERNAME + "=?", new String[] {username});
        ArrayList<Order> orderList = new ArrayList<>();
        if (cursor.moveToFirst())
        {
            do {
                Order order = new Order();
                order.setUsername(cursor.getString(cursor.getColumnIndex(Util.USERNAME)));
                order.setFlowerImageBytes(cursor.getBlob(cursor.getColumnIndex(Util.FLOWER_IMAGE)));
                order.setReceiverName(cursor.getString(cursor.getColumnIndex(Util.RECEIVER_NAME)));
                order.setDate(cursor.getString(cursor.getColumnIndex(Util.DATE)));
                order.setTime(cursor.getString(cursor.getColumnIndex(Util.TIME)));
                order.setDestination(cursor.getString(cursor.getColumnIndex(Util.DESTINATION)));
                order.setDestinationLatitude(cursor.getDouble(cursor.getColumnIndex(Util.DESTINATION_LATITUDE)));
                order.setDestinationLongitude(cursor.getDouble(cursor.getColumnIndex(Util.DESTINATION_LONGITUDE)));
                order.setFlowerType(cursor.getString(cursor.getColumnIndex(Util.FLOWER_TYPE)));
                order.setQuantity(cursor.getString(cursor.getColumnIndex(Util.QUANTITY)));
                order.setMessage(cursor.getString(cursor.getColumnIndex(Util.MSG)));
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        return orderList;
    }
}

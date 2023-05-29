package com.example.mysearchingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.example.mysearchingapp.util.Util;


public class UserDatabaseHelper extends SQLiteOpenHelper {

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, Util.USER_DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.USER_TABLE_NAME + " ("
                + Util.USER_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.PROFILE_PICTURE
                + " BLOB , "
                + Util.NAME
                + " TEXT , "
                + Util.USERNAME
                + " TEXT , "
                + Util.PASSWORD
                + " TEXT , "
                + Util.PHONE_NO
                + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_USER_TABLE, new String[]{Util.USER_TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertUser(User user) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.PROFILE_PICTURE, user.getImage());
        contentValues.put(Util.NAME, user.getName());
        contentValues.put(Util.USERNAME, user.getUsername());
        contentValues.put(Util.PASSWORD, user.getPassword());
        contentValues.put(Util.PHONE_NO, user.getPhoneNo());

        long rowId = sqLiteDatabase.insert(Util.USER_TABLE_NAME, null, contentValues);
        return rowId;
    }

    //Get user from username
    @SuppressLint("Range")
    public User getUser(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.USER_TABLE_NAME + " WHERE " + Util.USERNAME + "=?", new String[]{username});
        Log.i("tes",cursor.getCount()+"");
        User user = new User();
        if(cursor.moveToFirst()){
            do{
                Log.i("tes", "tes");
                user.setImage(cursor.getBlob(cursor.getColumnIndex(Util.PROFILE_PICTURE)));
                user.setName(cursor.getString(cursor.getColumnIndex(Util.NAME)));
                user.setUsername(username);
                user.setPassword(cursor.getString(cursor.getColumnIndex(Util.PASSWORD)));
                user.setPhoneNo(cursor.getString(cursor.getColumnIndex(Util.PHONE_NO)));

            } while(cursor.moveToNext());
        }
        return user;
    }

    // Check is user exists from their username
    public boolean userExists(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.USER_TABLE_NAME, new String[]{Util.USER_ID}, Util.USERNAME + "=?", new String[]{username}, null, null, null);
        int numrows = cursor.getCount();
        db.close();

        if(numrows>0) {
            return true;
        }
        else {
            return false;
        }
    }

    //Get user with username and password
    public boolean fetchUser(String username, String password) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                Util.USER_TABLE_NAME,
                new String[]{Util.USER_ID},
                Util.USERNAME + "=? and " + Util.PASSWORD + "=?",
                new String[]{username, password},
                null,
                null,
                null);

        int numberOfRows = cursor.getCount();
        sqLiteDatabase.close();
        if (numberOfRows > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    //Update user details
    public int updateDetails(String username, ContentValues contentValues)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.update(Util.USER_TABLE_NAME, contentValues, Util.USERNAME + "=?", new String[] {username});
        return res;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

}


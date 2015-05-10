package com.example.shivam.imagefeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Shivam on 08/05/15 at 12:12 PM.
 */
public class PostORM {

    private static final String TAG = "PostORM";
    public static final String TABLE_NAME = "post";
    private static final String COLUMN_URI = "uri";
    private static final String COLUMN_COORDINATES = "coordinates";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_TIME = "time";
    private DatabaseWrapper dw;
    SQLiteDatabase myDataBase;

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_URI + " TEXT, "
                    + COLUMN_COORDINATES + " TEXT, "
                    + COLUMN_ADDRESS + " TEXT, "
                    + COLUMN_TIME + " TEXT)";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public int insertPost(Context c,String uri,String coordinates,String address,String time)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(c);
        myDataBase = databaseWrapper.getWritableDatabase();
        long postId = 0;
        if (isDatabaseOpened()) {
            ContentValues values = new ContentValues();
            values.put(PostORM.COLUMN_URI,uri);
            values.put(PostORM.COLUMN_COORDINATES,coordinates);
            values.put(PostORM.COLUMN_ADDRESS,address);
            values.put(PostORM.COLUMN_TIME,time);
            postId = myDataBase.insert(PostORM.TABLE_NAME, "null", values);
            Log.e(TAG, "Inserted new Post with ID: " + postId);
            myDataBase.close();
        }
        return (int) postId;
    }

    public ArrayList<String> getUrifromDB(Context c)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(c);
        myDataBase = databaseWrapper.getWritableDatabase();
        ArrayList<String> uris = new ArrayList<String>();
        Cursor cur = myDataBase.rawQuery("SELECT * from post",null);
        Log.e("COUNT",String.valueOf(cur.getCount()));
        for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
        {
            uris.add(cur.getString(0));
        }
        System.out.println(uris);
        return uris;
    }

    public ArrayList<String> getCoordinatesfromDB(Context c)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(c);
        myDataBase = databaseWrapper.getWritableDatabase();
        ArrayList<String> coordinates = new ArrayList<String>();
        Cursor cur = myDataBase.rawQuery("SELECT * from post",null);
        Log.e("COUNT",String.valueOf(cur.getCount()));
        for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
        {
            coordinates.add(cur.getString(1));
        }
        System.out.println(coordinates);
        return coordinates;
    }

    public ArrayList<String> getAddressfromDB(Context c)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(c);
        myDataBase = databaseWrapper.getWritableDatabase();
        ArrayList<String> addresses = new ArrayList<String>();
        Cursor cur = myDataBase.rawQuery("SELECT * from post",null);
        Log.e("COUNT",String.valueOf(cur.getCount()));
        for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
        {
            addresses.add(cur.getString(2));
        }
        System.out.println(addresses);
        return addresses;
    }

    public ArrayList<String> getTimefromDB(Context c)
    {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(c);
        myDataBase = databaseWrapper.getWritableDatabase();
        ArrayList<String> times = new ArrayList<String>();
        Cursor cur = myDataBase.rawQuery("SELECT * from post",null);
        Log.e("COUNT",String.valueOf(cur.getCount()));
        for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
        {
            times.add(cur.getString(3));
        }
        System.out.println(times);
        return times;
    }


    public boolean isDatabaseOpened() {
        if (myDataBase == null) {
            return false;
        } else {
            return myDataBase.isOpen();
        }
    }






}

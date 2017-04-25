package com.sazal.siddiqui.gisdemo.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sazal.siddiqui.gisdemo.Model.Package;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sazal on 2017-02-12.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "GPS_DEMO";

    private static final String TABLE_PACKAGE = "info";

    private static final String KEY_CREATED_ON = "createdOn";
    private static final String KEY_CREATED_BY = "createdBy";
    private static final String KEY_UPDATED_ON = "updatedON";
    private static final String KEY_UPDATED_BY = "updatedBy";

    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    private static final String KEY_ID = "_id";

    private static final String CREATE_TABLE_PACKAGE = "CREATE TABLE " + TABLE_PACKAGE +" (" +
            KEY_ID +" INTEGER PRIMARY KEY,"+
            KEY_NAME +" TEXT NOT NULL," +
            KEY_ADDRESS +" INTEGER NOT NULL,"+
            KEY_LAT +" REAL NOT NULL,"+
            KEY_LNG +" REAL NOT NULL,"+
            KEY_CREATED_ON +" DATETIME,"+
            KEY_UPDATED_ON +" DATETIME,"+
            KEY_CREATED_BY + " INTEGER,"+
            KEY_UPDATED_BY +" INTEGER"+ ")";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB","onCreate");
        db.execSQL(CREATE_TABLE_PACKAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG,"onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PACKAGE);

        onCreate(db);
    }


    public long insertPackage(Package aPackage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, aPackage.getName());
        values.put(KEY_LAT,aPackage.getLat());
        values.put(KEY_LNG,aPackage.getLng());
        values.put(KEY_ADDRESS, aPackage.getAddress());
        values.put(KEY_CREATED_ON, getDateTime());

        long r = db.insert(TABLE_PACKAGE,null,values);
        closeDB();
        return r;
    }

    public List<Package> getAllPackage(){
        List<Package> customerTypes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PACKAGE;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Package customerType = new Package();
                customerType.setPackageId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                customerType.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                customerType.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                customerType.setLat(cursor.getDouble(cursor.getColumnIndex(KEY_LAT)));
                customerType.setLng(cursor.getDouble(cursor.getColumnIndex(KEY_LNG)));

                customerTypes.add(customerType);
            }while (cursor.moveToNext());
        }

        return customerTypes;
    }

    private void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}

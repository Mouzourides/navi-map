package com.mouzourides.navi_map;

/**
 * Created by Nik Mouzourides on 19/02/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
//Class that models database and contains methods that return data from database
public class DatabaseHelper extends SQLiteOpenHelper {
    // database names
    private static final String DBNAME = "Locations";
    public static final String LocName = "LocationName";
    public static final String LocID = "LocationID";
    public static final String LocNotes = "LocationNotes";
    public static final String LocDesc = "LocationDescription";
    public static final String LocImage = "ImageID";
    public static final String LocVisited = "Visited";
    public static final String LocRating = "LocationRating";
    public static final String LocOpenNow = "LocationOpenNow";
    public static final String LocLat = "LocationLat";
    public static final String LocLng = "LocationLng";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the locations database
        String sql = "CREATE TABLE " + DBNAME + "("
                + LocID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LocName + " TEXT, "
                + LocNotes + " TEXT, "
                + LocDesc + " TEXT, "
                + LocImage + " TEXT, "
                + LocVisited + " INTEGER, "
                + LocRating + " REAL, "
                + LocOpenNow + " TEXT, "
                + LocLat + " REAL, "
                + LocLng + " REAL "
                + " );";
        Log.i("DBHELPER", sql);
        db.execSQL(sql);
    }
    //method for inserting data into the database
    public void insertData(
            String name, String notes, String desc, String photoID, int visited,
            float rating, String openNow, float lat, float lng
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocName, name);
        contentValues.put(LocNotes, notes);
        contentValues.put(LocDesc, desc);
        contentValues.put(LocImage, photoID);
        contentValues.put(LocVisited, visited);
        contentValues.put(LocRating, rating);
        contentValues.put(LocOpenNow, openNow);
        contentValues.put(LocLat, lat);
        contentValues.put(LocLng, lng);
        db.insert(DBNAME, null, contentValues);
    }

    //empties database
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DBNAME);
    }

    // deletes a row where LocName = names
    public void deleteRow(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DBNAME + " where " + LocName + "='" + name + "'");
    }

    //updates LocNotes where LocName = name
    public void updateNotes(String notes, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+ DBNAME + " set " + LocNotes + "='" + notes + "' where " + LocName + " = '"+ name + "'" );
    }

    // gets lat where LocName = name
    public String[] getLatFromName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+ LocLat + " FROM " + DBNAME + " WHERE " + LocName + " ='" + name + "'" ,null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex(LocLat)));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    // gets long where LocName = name
    public String[] getLngFromName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+ LocLng + " FROM " + DBNAME + " WHERE " + LocName + " ='" + name + "'" ,null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex(LocLng)));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    //gets LocImage and LocDesc where LocName = name
    public String[] getDataFromName(String name){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+ DBNAME +" WHERE " + LocName + "='"+ name + "'" , null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
                names.add(cursor.getString(cursor.getColumnIndex(LocImage)));
                names.add(cursor.getString(cursor.getColumnIndex(LocDesc)));
                cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    // gets an amount of random names from the database
    public String[] getRandom(int amount){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+ LocName +" FROM "+ DBNAME +" ORDER BY RANDOM() LIMIT " + amount , null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex(LocName)));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    // gets all names from database
    public String[] getNames(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocName +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex(LocName)));
            cursor.moveToNext();
        }
        cursor.close();
        return names.toArray(new String[names.size()]);
    }

    // gets all notes from database
    public String[] getNotes(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocNotes +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> notes = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            notes.add(cursor.getString(cursor.getColumnIndex(LocNotes)));
            cursor.moveToNext();
        }
        cursor.close();
        return notes.toArray(new String[notes.size()]);
    }

    // gets all images from database
    public String[] getImage(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocImage +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> imageID = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            imageID.add(cursor.getString(cursor.getColumnIndex(LocImage)));
            cursor.moveToNext();
        }
        cursor.close();
        return imageID.toArray(new String[imageID.size()]);
    }

    // gets all desc from database
    public String[] getDesc(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocDesc +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> desc = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            desc.add(cursor.getString(cursor.getColumnIndex(LocDesc)));
            cursor.moveToNext();
        }
        cursor.close();
        return desc.toArray(new String[desc.size()]);
    }

    // gets all ratings from database
    public String[] getRating(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocRating +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> rating = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            rating.add(cursor.getString(cursor.getColumnIndex(LocRating)));
            cursor.moveToNext();
        }
        cursor.close();
        return rating.toArray(new String[rating.size()]);
    }

    // gets all open now data from database
    public String[] getOpenNow(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + LocOpenNow +" FROM " + DBNAME , null);
        cursor.moveToFirst();
        ArrayList<String> openNow = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            openNow.add(cursor.getString(cursor.getColumnIndex(LocOpenNow)));
            cursor.moveToNext();
        }
        cursor.close();
        return openNow.toArray(new String[openNow.size()]);
    }

    // on upgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  DBNAME);
        onCreate(db);
    }

}

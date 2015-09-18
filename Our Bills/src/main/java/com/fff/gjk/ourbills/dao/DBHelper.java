package com.fff.gjk.ourbills.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gjk on 3/3/14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MainDB";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory is set null
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate is called when DB is built the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS friends (fid INTEGER PRIMARY KEY AUTOINCREMENT, fname VARCHAR UNIQUE NOT NULL, gender VARCHAR NOT NULL, email VARCHAR, pic VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS groups (gid INTEGER PRIMARY KEY AUTOINCREMENT, gname VARCHAR UNIQUE NOT NULL, gcid INTEGER, pic VARCHAR, FOREIGN KEY(gcid) REFERENCES gcategory(gcid))");
        db.execSQL("CREATE TABLE IF NOT EXISTS bills (bid INTEGER PRIMARY KEY AUTOINCREMENT, gid INTEGER NOT NULL, title VARCHAR NOT NULL, amount DOUBLE NOT NULL, isSimple INTEGER NOT NULL, bcid INTEGER, date VARCHAR NOT NULL, pic VARCHAR, FOREIGN KEY(bcid) REFERENCES bcategory(bcid))");
        db.execSQL("CREATE TABLE IF NOT EXISTS friend_group (fid INTEGER, gid INTEGER, balance DOUBLE, FOREIGN KEY(fid) REFERENCES friends(fid), FOREIGN KEY(gid) REFERENCES groups(gid),PRIMARY KEY(fid,gid))");
        db.execSQL("CREATE TABLE IF NOT EXISTS friend_bill (fid INTEGER, bid INTEGER, pay INTEGER, owe INTEGER, FOREIGN KEY(fid) REFERENCES friends(fid), FOREIGN KEY(bid) REFERENCES bills(bid))");
        db.execSQL("CREATE TABLE IF NOT EXISTS gcategory (gcid INTEGER PRIMARY KEY, gcname VARCHAR UNIQUE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS bcategory (bcid INTEGER PRIMARY KEY, bcname VARCHAR UNIQUE)");

        db.execSQL("INSERT INTO gcategory VALUES(?, ?)", new Object[]{1, "trip"});
        db.execSQL("INSERT INTO gcategory VALUES(?, ?)", new Object[]{2, "living"});
        db.execSQL("INSERT INTO gcategory VALUES(?, ?)", new Object[]{3, "hang out"});
        db.execSQL("INSERT INTO gcategory VALUES(?, ?)", new Object[]{4, "other"});

        db.execSQL("INSERT INTO bcategory VALUES(?, ?)", new Object[]{1, "meal"});
        db.execSQL("INSERT INTO bcategory VALUES(?, ?)", new Object[]{2, "activity"});
        db.execSQL("INSERT INTO bcategory VALUES(?, ?)", new Object[]{3, "transportation"});
        db.execSQL("INSERT INTO bcategory VALUES(?, ?)", new Object[]{4, "shopping"});
        db.execSQL("INSERT INTO bcategory VALUES(?, ?)", new Object[]{5, "other"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
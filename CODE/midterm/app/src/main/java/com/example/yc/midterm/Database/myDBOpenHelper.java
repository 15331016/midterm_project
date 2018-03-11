package com.example.yc.midterm.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaiminglee on 14/11/2017.
 */

public class myDBOpenHelper extends SQLiteOpenHelper {
    public myDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE person(" +
                "personid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "imageId INTEGER," +
                "name TEXT," +
                "sex TEXT," +
                "birthday TEXT," +
                "styleName TEXT," +
                "birthPlace TEXT," +
                "birthPlaceNow TEXT," +
                "power TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}


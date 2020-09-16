package com.ose4g.typerighter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScoreOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TypeRighterHighScores.db";
    public static final int DATABASE_VERSION = 1;

    public HighScoreOpenHelper( Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HighScoreDatabaseContract.HighScoreEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

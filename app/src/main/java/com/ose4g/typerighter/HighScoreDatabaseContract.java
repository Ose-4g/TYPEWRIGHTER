package com.ose4g.typerighter;

import android.provider.BaseColumns;

public final  class HighScoreDatabaseContract
{
    private HighScoreDatabaseContract(){}

    public static final class HighScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "high_scores";
        public static final String GAME_LEVEL = "level_reached";
        public static final String LEVEL_PROGRESS = "level_progress";

        //TO CREATE THE TABLE NOW
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
        TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY " +
                GAME_LEVEL + " INTEGER NOT NULL, " +
                LEVEL_PROGRESS + " INTEGER NOT NULL)";
    }
}

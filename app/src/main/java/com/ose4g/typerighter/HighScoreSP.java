package com.ose4g.typerighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HighScoreSP
{
    private HighScoreSP(){

    }
    public static final String NAME= "HighScoresPreferences";
    public static ArrayList<Long> allScores ;

    public static SharedPreferences getPrefs(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getAll().isEmpty())
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(int i=0;i<20;i++)
            {
                editor.putLong(i+"",(long)0 );
            }
            editor.apply();
        }

        return sharedPreferences;
    }

    public static long getScore(Context context, String key)
    {
        return getPrefs(context).getLong(key,-1);
    }

    public static void putScore(Context context,String key, long score)
    {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putLong(key,score);
        editor.apply();
        //Log.i("SP","ADDED"+score);
    }

    public static ArrayList<Long> getAllScores(Context context)
    {
        allScores = new ArrayList<>();
        for(int i=0;i<20;i++)
        {
            allScores.add(getScore(context,i+""));
            //Log.i("SP","ADDED"+getScore(context,i+""));
        }
        Collections.sort(allScores);;
        return allScores;
    }

    public static void updateSP(Context context)
    {
        if(allScores!=null)
        {
            Collections.sort(allScores);
            for(int i=0;i<20;i++)
            {
                putScore(context,i+"",allScores.get(i));
                //Log.i("SP","UPDATED"+getScore(context,i+""));
            }

        }
    }

}

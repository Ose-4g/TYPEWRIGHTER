package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class HighScores extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Long> ScoresList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        ScoresList = HighScoreSP.getAllScores(this);
        Collections.reverse(ScoresList);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(new HighScoreAdapter(this,ScoresList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
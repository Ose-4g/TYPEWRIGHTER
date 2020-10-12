package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ose4g.typerighter.Adapters.HighScoreAdapter;
import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

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

        findViewById(R.id.board_global).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HighScores.this, GoogleSignInActivity.class));
            }
        });

    }
}
package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.ose4g.typerighter.Adapters.HighScoreAdapter;
import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

import java.util.ArrayList;
import java.util.Collections;

public class HighScores extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Long> ScoresList;
    private LinearLayout mLinearLayout;
    private Runnable mRunnable1;
    private Runnable mRunnable2;
    private Handler mHandler;


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

        final Animation scaleIn = AnimationUtils.loadAnimation(this,R.anim.scale_in_small);
        final Animation scaleOut = AnimationUtils.loadAnimation(this, R.anim.scale_out_small);

        mHandler = new Handler(Looper.getMainLooper());

        mLinearLayout = findViewById(R.id.board_global);
        mRunnable1 = new Runnable() {
            @Override
            public void run() {
                scaleView(mLinearLayout,(float)1.0,(float)1.1);
                mHandler.postDelayed(mRunnable2,500);
            }
        };

        mRunnable2 = new Runnable() {
            @Override
            public void run() {

                scaleView(mLinearLayout,(float)1.1,(float)1.0);
                mHandler.postDelayed(mRunnable1,500);
            }
        };

        mRunnable1.run();

    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        v.startAnimation(anim);
    }
}
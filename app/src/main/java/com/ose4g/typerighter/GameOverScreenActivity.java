package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;

public class GameOverScreenActivity extends AppCompatActivity {

    public static final String GAME_SCORE = "com.ose4g.typerighter.GAME_SCORE";
    public static final String NEW_HIGH_SCORE = "com.ose4g.typerighter.NEW_HIGH_SCORE";
    public static final long DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        Animation zoom_out_entry = AnimationUtils.loadAnimation(this,R.anim.zoom_out_entry);
        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        findViewById(R.id.gold_badge).setVisibility(View.INVISIBLE);
        findViewById(R.id.high_score_desc).setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        long score = intent.getLongExtra(GAME_SCORE, DEFAULT);
        boolean newHighScore = intent.getBooleanExtra(NEW_HIGH_SCORE,false);

        long best_score = Collections.max(HighScoreSP.getAllScores(getApplicationContext()));


        ((TextView) findViewById(R.id.curr_score)).setText(score+"");
        ((TextView) findViewById(R.id.best_score)).setText(best_score+"");

        if(newHighScore) {
            ((ImageView)findViewById(R.id.gold_badge)).setVisibility(View.VISIBLE);
            findViewById(R.id.high_score_desc).setVisibility(View.VISIBLE);
            findViewById(R.id.gold_badge).startAnimation(zoom_out_entry);
            findViewById(R.id.high_score_desc).startAnimation(fade_in);
        }

        findViewById(R.id.backHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.replay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameOverScreenActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
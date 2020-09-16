package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverScreenActivity extends AppCompatActivity {

    public static final String GAME_SCORE = "com.ose4g.typerighter.GAME_SCORE";
    public static final long DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        Intent intent = getIntent();
        long score = intent.getLongExtra(GAME_SCORE, DEFAULT);

        ((TextView) findViewById(R.id.curr_score)).setText(score+"");

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
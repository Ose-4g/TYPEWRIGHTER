package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class GameOptionsScreen extends AppCompatActivity {

    private final int[] drawables= {
            R.id.play,
            R.id.how_to_play,
            R.id.high_scores};
    private Animation slide_in_from_left;




    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options_screen);

        slide_in_from_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_from_left);







        ((ImageView) findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOptionsScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ((ImageView) findViewById(R.id.how_to_play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOptionsScreen.this, HowToPlayActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.high_scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameOptionsScreen.this,HighScores.class));
            }
        });




        mHandler = new Handler();

        enter_the_game();


    }

    private void enter_the_game() {

        for(int i=0;i<drawables.length;i++)
        {
            ((ImageView) findViewById(drawables[i])).setVisibility(View.INVISIBLE);
        }


        Runnable runnable1 = new Runnable() {
            int i=0;
            @Override
            public void run() {
                ((ImageView) findViewById(drawables[i])).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(drawables[i])).startAnimation(slide_in_from_left);
                i++;
                if(i>=drawables.length)
                {

                }
                else
                {
                    (new Handler()).postDelayed(this,300);
                }
            }
        };

        mHandler.postDelayed(runnable1,300);

    }

    @Override
    protected void onResume() {
        super.onResume();
        enter_the_game();
    }


}
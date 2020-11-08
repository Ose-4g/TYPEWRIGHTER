package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;


public class GameSplashScreen extends AppCompatActivity {

    private ImageView ose4g;
    private ImageView type_righter;


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_splash_screen);
        ose4g = findViewById(R.id.ose4g);
        type_righter = findViewById(R.id.type_righter);

        ose4g.setVisibility(View.INVISIBLE);
        type_righter.setVisibility(View.INVISIBLE);





        final Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);




        mHandler = new Handler(Looper.getMainLooper());
        final int[] drawables = {R.id.ose4g,R.id.type_righter};

        Runnable runnable = new Runnable() {
            int i=0;
            @Override
            public void run() {
                final ImageView image = findViewById(drawables[i]);
                image.setVisibility(View.VISIBLE);
                image.startAnimation(fade_in);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        image.startAnimation(fade_out);
                        image.setVisibility(View.INVISIBLE);
                    }
                }, 1500);

                i++;
                if(i<2)
                    mHandler.postDelayed(this,2000);
                else
                {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(GameSplashScreen.this,GameOptionsScreen.class);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out );
                            startActivity(intent);
                            finish();
                        }
                    },1500);

                }
            }
        };


        mHandler.postDelayed(runnable,500);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
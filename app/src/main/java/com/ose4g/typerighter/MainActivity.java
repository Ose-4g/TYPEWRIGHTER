package com.ose4g.typerighter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static  String GAME_DIFFICULTY = "com.ose4g.typerighter.GAME_DIFFICULTY";
    private TextView mPlay_countdown;
    private Handler mHandler;
    private int difficulty;
    private TextView question1;
    private TextView question2;
    private TextView question3;
    private EditText answer;
    private boolean isPaused;
    private boolean game_ending;
    private Animation mZoom_entry;
    private Animation mFade_out;
    final private long starting_value = 70;
    private long curr_value = 70;
    private int strikes;
    private long score;
    private boolean just_starting;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mQuestion;
    private float mQuestion_position;
    private float mCenter;
    private float mStep;
    private Runnable mGamePlay;
    private float mQuestion_height;
    private int curr_tv;
    private TextView[] questions;
    private String[] mValues;
    private float mCurr_pos;
    private TextWatcher mTextwatcher;
    private boolean done_one_time;


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!done_one_time)
        {
            mQuestion_position = getPosition(findViewById(R.id.score));
            mQuestion_height = mQuestion.getHeight();
            mCenter = getPosition(findViewById(R.id.limit));
            findViewById(R.id.limit).bringToFront();
            mStep = (mCenter-mQuestion_position)/100;
            mCurr_pos = mQuestion_position;
            done_one_time=true;
        }


        mGamePlay = new Runnable() {

            @Override
            public void run() {

                if(mCurr_pos<mCenter)
                {
                    mCurr_pos += mStep;
                    mHandler.postDelayed(this,curr_value);
                }
                else
                {
                    mHandler.removeCallbacks(this);
                    if(score<1000)
                        game_over_screen();
                    else
                        alertYouLose();
                }
                mQuestion.setY(mCurr_pos);

            }
        };


        mTextwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(questions[curr_tv].getText().toString().equals(charSequence.toString()))
                {
                    appear(false,questions[curr_tv]);
                    curr_tv+=1;
                    curr_tv%=3;
                    if(curr_tv==0)
                    {
                        score+=100;
                        ((TextView)findViewById(R.id.score)).setText(score+"");
                        if(score%200==0)
                            curr_value = Math.max(5,curr_value-5);

                        mHandler.removeCallbacks(mGamePlay);
                        nextLevel();
                    }
                }
                answer.removeTextChangedListener(mTextwatcher);
                answer.setText("");
                answer.addTextChangedListener(mTextwatcher);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_countdowm);

        curr_tv=0;
        mQuestion = (LinearLayout) findViewById(R.id.question);
        isPaused = false;
        just_starting=true;
        done_one_time = false;
        game_ending = false;
        mZoom_entry = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_entry);
        mFade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        mPlay_countdown = findViewById(R.id.play_countdown);
        strikes = 0;
        score = 0;
        mHandler = new Handler();

        mValues = new String[]{"aba","abs","ace","act","add","ado","aft","age","ago","aha","aid","aim","air","ala","ale","all","alt","amp","ana","and","ant","any",
                "ape","app","apt","arc","are","ark","arm","art","ash","ask","asp","ass","ate","ave","awe","axe","aye","BAA","bad","bag","ban","bar","bat","bay",
                "bed","bee","beg","bel","ben","bet","bid","big","bin","bio","bis","bit","biz","bob","bog","boo","bow","box","boy","bra","bud","Bug","bum","bun",
                "bus","but","buy","bye","cab","cad","cam","can","cap","car","cat","chi","cob","cod","col","con","coo","cop","cor","cos","cot","cow","cox","coy",
                "cry","cub","cue","cum","cup","cut","dab","dad","dal","dam","dan","day","Dee","def","del","den","dew","did","die","dig","dim","din","dip","dis",
                "doc","doe","dog","don","dot","dry","dub","due","dug","dun","duo","dye","ear","eat","ebb","ecu","eft","egg","ego","elf","elm","emu","end","era",
                "eta","eve","eye","fab","fad","fan","far","fat","fax","fay","fed","fee","fen","few","fig","fin","fir","fit","fix","flu","fly","foe","fog","for",
                "fox","fry","fun","fur","gag","gal","gap","gas","gay","gee","gel","gem","get","gig","gin","god","got","gum","gun","gut","guy","gym","had","ham",
                "has","hat","hay","hem","hen","her","hey","hid","him","hip","his","hit","hog","hon","hop","hot","how","hub","hue","hug","huh","hum","hut","ice",
                "icy","igg","ill","imp","ink","inn","ion","its","ivy","jam","jar","jaw","jay","jet","jew","job","joe","jog","joy","jug","jun","kay","ken","key",
                "kid","kin","kit","lab","lac","lad","lag","lam","lap","law","lax","lay","lea","led","Lee","leg","les","let","lib","lid","lie","lip","lit","log",
                "lot","low","mac","mad","mag","man","map","mar","mas","mat","max","may","med","meg","men","Met","mid","mil","mix","mob","mod","mol","mom","mon",
                "mop","mot","mud","mug","mum","nab","nah","nan","nap","nay","neb","neg","net","new","nil","nip","nod","nor","nos","not","now","nun","nut","oak",
                "odd","off","oft","oil","old","ole","one","ooh","opt","orb","ore","our","out","owe","owl","own","pac","pad","pal","pam","pan","pap","par","pas",
                "pat","paw","pay","pea","peg","pen","pep","per","pet","pew","phi","pic","pie","pig","pin","pip","pit","ply","pod","pol","pop","pot","pro","psi",
                "pub","pup","put","rad","rag","raj","ram","ran","rap","rat","raw","ray","red","ref","reg","rem","rep","rev","rib","rid","rig","rim","rip","rob",
                "rod","roe","rot","row","rub","rue","rug","rum","run","rye","sab","sac","sad","sae","sag","sal","sap","sat","saw","say","sea","sec","see","sen",
                "set","sew","sex","she","shy","sic","sim","sin","sip","sir","sis","sit","six","ski","sky","sly","sod","sol","son","sow","soy","spa","spy","sub",
                "sue","sum","sun","sup","tab","tad","tag","tam","tan","tap","tar","tat","tax","tea","ted","tee","ten","the","thy","tie","tin","tip","tod","toe",
                "tom","ton","too","top","tor","tot","tow","toy","try","tub","tug","two","use","van","vat","vet","via","vie","vow","wan","war","was","wax","way",
                "web","wed","wee","wet","who","why","wig","win","wis","wit","won","woo","wow","wry","wye","yen","yep","yes","yet","you","zip","zoo"};

        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        question3 = findViewById(R.id.question3);
        answer = findViewById(R.id.answer);
        questions = new TextView[]{question1,question2,question3};




        play_countdown();

    }

    @Override
    public void onBackPressed() {
        if(!isPaused && !just_starting)
            pause_dialog();
    }

    private void pause_dialog() {
        pauseGame();
        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        }
        catch(Exception e)
        {

        }
        answer.removeTextChangedListener(mTextwatcher);
        mLayoutInflater = LayoutInflater.from(MainActivity.this);
        findViewById(R.id.question).bringToFront();
        final View alertView = mLayoutInflater.inflate(R.layout.confirm_pause_play,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(alertView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        ((TextView) alertView.findViewById(R.id.back_to_game)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                play_countdown();
            }
        });

        ((TextView) alertView.findViewById(R.id.go_to_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    private  void pauseGame()
    {
        isPaused=true;
        mHandler.removeCallbacks(mGamePlay);

    }


    private void play_countdown() {

        ((LinearLayout) findViewById(R.id.countdown_background)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.score)).setText(score+"");
        mHandler.postDelayed(new Runnable() {
            int i=3;
            @Override
            public void run() {
                mPlay_countdown.setText(i+"");
                mPlay_countdown.startAnimation(mZoom_entry);
                i--;
                if(i>=0)
                    mHandler.postDelayed(this,1000);
                else
                {
                    ((LinearLayout) findViewById(R.id.countdown_background)).setVisibility(View.GONE);
                    mHandler.removeCallbacks(this);
                    mPlay_countdown.setText("");
                    if(isPaused)
                    {
                        isPaused=false;
                        mHandler.postDelayed(mGamePlay,curr_value);

                    }
                    else if(just_starting)
                    {
                        nextLevel();

                    }


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    answer.requestFocus();
                    answer.addTextChangedListener(mTextwatcher);
                }
            }
        }, 500);
    }




    private void appear(boolean show,View view)
    {
        if(show)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.INVISIBLE);
    }




    private void nextLevel()
    {
        mCurr_pos = mQuestion_position;
        String word = mValues[new Random().nextInt(mValues.length)];
        for(int i=0;i<3;i++)
        {
            questions[i].setVisibility(View.VISIBLE);
            questions[i].setText(word.charAt(i)+"");
        }
        if(just_starting) {

            mHandler.postDelayed(mGamePlay, starting_value);
            just_starting=false;
        }
        else
            mHandler.postDelayed(mGamePlay,curr_value);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        }
        catch(Exception e)
        {

        }
        if(!game_ending)
        {
            if(!isPaused && !just_starting )
                pause_dialog();
            else {
                mHandler.removeCallbacksAndMessages(null);
                pause_dialog();
            }
        }

    }

    private float getPosition(View view)
    {

        int[] pos =new int[2];
        view.getLocationInWindow(pos);
        return pos[1];
    }


    private void alertYouLose()
    {

        // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(answer.getWindowToken(), 0);
        answer.removeTextChangedListener(mTextwatcher);
        mLayoutInflater = LayoutInflater.from(MainActivity.this);
        final View view = mLayoutInflater.inflate(R.layout.alert_you_lose,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        ((ProgressBar) view.findViewById(R.id.loser_countdown)).setMax(10000);

        final CountDownTimer timer = new CountDownTimer(10000,1) {
            @Override
            public void onTick(long l) {
                ((ProgressBar) view.findViewById(R.id.loser_countdown)).setProgress((int) l);
                ((TextView) view.findViewById(R.id.loser_count)).setText(""+(int) (l/1000+1));
            }

            @Override
            public void onFinish() {
                if(alertDialog.isShowing())
                {
                    alertDialog.dismiss();
                    game_over_screen();
                }

            }
        };


        ((TextView) view.findViewById(R.id.keep_playing)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();
                alertDialog.dismiss();
                score -= 1000;
                mCurr_pos = mQuestion_position;
                just_starting=true;
                isPaused=false;
                play_countdown();

            }
        });

        ((TextView) view.findViewById(R.id.skip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                alertDialog.dismiss();
                game_over_screen();
            }
        });


        timer.start();
    }


    private void game_over_screen()
    {
        game_ending = true;
        boolean newHighScore = false;
        //SharedPreferences sharedPreferences = HighScoreSP.getPrefs(getApplicationContext());
        ArrayList<Long> allScores = HighScoreSP.getAllScores(getApplicationContext());
        if(score<Collections.min(allScores))//if this is lower than the lowest score
        {

        }
        else//it beats one of the previous scores
        {
            if(score> Collections.max(allScores))
                newHighScore=true;

            allScores.remove(0);
            allScores.add(score);
            HighScoreSP.updateSP(getApplicationContext());

        }
        Intent intent = new Intent(MainActivity.this,GameOverScreenActivity.class);
        intent.putExtra(GameOverScreenActivity.GAME_SCORE, score);
        intent.putExtra(GameOverScreenActivity.NEW_HIGH_SCORE,newHighScore);
        startActivity(intent);
        finish();
    }
    
}



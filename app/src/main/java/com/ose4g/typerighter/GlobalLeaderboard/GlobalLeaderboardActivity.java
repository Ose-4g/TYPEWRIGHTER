package com.ose4g.typerighter.GlobalLeaderboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ose4g.typerighter.Adapters.GlobalLeaderBoardAdapter;
import com.ose4g.typerighter.Firebase.FireBaseInstance;
import com.ose4g.typerighter.MainActivity;
import com.ose4g.typerighter.Models.User;
import com.ose4g.typerighter.R;
import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalLeaderboardActivity extends AppCompatActivity {

    ArrayList<User> mUsers;
    GlobalLeaderBoardAdapter mGlobalLeaderBoardAdapter;
    private ChildEventListener mChildEventListener;
    private RecyclerView mRecyclerView;
    GoogleSignInClient mGoogleSignInClient;
    private String mUsername;
    private GlobalLeaderboardViewModel mViewModel;
    private LayoutInflater mLayoutInflater;
    private TextView fineMe;
    private Animation circleExit;
    private Animation circleEnter;
    private int mUserPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_leaderboard);

        circleEnter = AnimationUtils.loadAnimation(this, R.anim.circular_scale_entry);
        circleExit = AnimationUtils.loadAnimation(this, R.anim.circular_scale_exit);

        mViewModel = new ViewModelProvider(this).get(GlobalLeaderboardViewModel.class);
        mViewModel.initializeValues(this);

        mUsername = "";


        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        fineMe = findViewById(R.id.find_me);

        mUsers = new ArrayList<>();

        mGlobalLeaderBoardAdapter = new GlobalLeaderBoardAdapter(this,mUsers,mViewModel);
        mRecyclerView = findViewById(R.id.globall_leaderboard_here);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mGlobalLeaderBoardAdapter);
        mLayoutInflater = LayoutInflater.from(GlobalLeaderboardActivity.this);




        findViewById(R.id.change_username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View alertView = mLayoutInflater.inflate(R.layout.alert_edit_name,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GlobalLeaderboardActivity.this);
                alertDialogBuilder.setView(alertView);
                alertDialogBuilder.setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                ((TextView) alertView.findViewById(R.id.update_username)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = ((EditText) alertView.findViewById(R.id.newName)).getText().toString();
                        mViewModel.updateUsername(name);

                        mViewModel.updated.observe(GlobalLeaderboardActivity.this, new Observer<Boolean>()
                        {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if(aBoolean)
                                {
                                    alertDialog.dismiss();
                                    Toast.makeText(GlobalLeaderboardActivity.this, getString(R.string.username_update_successful),Toast.LENGTH_SHORT).show();
                                    ((TextView) findViewById(R.id.username)).setText(name);
                                }
                                else
                                {
                                    Toast.makeText(GlobalLeaderboardActivity.this, getString(R.string.username_update_unsuccessful), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
            }
        });

        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View alertView = mLayoutInflater.inflate(R.layout.alert_logout,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GlobalLeaderboardActivity.this);
                alertDialogBuilder.setView(alertView);
                alertDialogBuilder.setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                ((TextView) alertView.findViewById(R.id.log_out_true)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        mViewModel.logout();
                        finish();
                    }
                });

                ((TextView) alertView.findViewById(R.id.log_out_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });



            }
        });





        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                if((mViewModel.mFirebaseUser.getUid()).equals(user.getUserId()))
                {
                    mUsername = user.getUserName();
                    mUserPosition = mUsers.size();
                }

                mUsers.add(user);
                mGlobalLeaderBoardAdapter.notifyItemInserted(mUsers.size()-1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                mUsername = user.getUserName();
                ((TextView) findViewById(R.id.username)).setText(mUsername);
                int index = mUsers.indexOf(user);
                mUsers.get(index).setUserName(user.getUserName());
                mUsers.get(index).setBestScore(user.getBestScore());
                mGlobalLeaderBoardAdapter.notifyItemChanged(index);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mViewModel.mQuery.removeEventListener(mChildEventListener);
                mGlobalLeaderBoardAdapter.clear();
                mViewModel.mQuery.addChildEventListener(mChildEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mViewModel.updateUserScore();
        findViewById(R.id.find_me).setVisibility(View.GONE);

        fineMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(mViewModel.mFirebaseUser.getUid(),"",(long)100);

                mRecyclerView.scrollToPosition(mUserPosition);

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && fineMe.getVisibility() == View.VISIBLE)
                {   //exiting the button
                    fineMe.startAnimation(circleExit);
                    fineMe.setVisibility(View.GONE);
                }
                else if(dy<0 && fineMe.getVisibility() != View.VISIBLE)
                {
                    fineMe.setVisibility(View.VISIBLE);
                    fineMe.startAnimation(circleEnter);
                }

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        mViewModel.currentUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mUsername = user.getUserName();
                ((TextView) findViewById(R.id.username)).setText(mUsername);
                mViewModel.mQuery.addChildEventListener(mChildEventListener);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                findViewById(R.id.find_me).setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.mQuery.removeEventListener(mChildEventListener);
        mGlobalLeaderBoardAdapter.clear();

    }


}
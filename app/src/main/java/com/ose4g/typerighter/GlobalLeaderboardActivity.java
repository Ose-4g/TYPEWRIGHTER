package com.ose4g.typerighter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ose4g.typerighter.Adapters.GlobalLeaderBoardAdapter;
import com.ose4g.typerighter.Firebase.FireBaseInstance;
import com.ose4g.typerighter.Models.User;
import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalLeaderboardActivity extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<User> mUsers;
    GlobalLeaderBoardAdapter mGlobalLeaderBoardAdapter;
    private Query mQuery;
    private Query mQueryAnother;
    private ChildEventListener mChildEventListener;
    private RecyclerView mRecyclerView;
    private FirebaseUser mFirebaseUser;
    GoogleSignInOptions mGoogleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;
    private String mUsername;
    private ChildEventListener mChildEventListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_leaderboard);

        mUsername = "";

        mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,mGoogleSignInOptions);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FireBaseInstance.getDatabaseReference().child("users");

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);


        mQuery = FireBaseInstance.getDatabaseReference().child("users").orderByChild("bestScore");
//        mQueryAnother = mDatabaseReference.equalTo(mFirebaseUser.getUid()).limitToFirst(1);
//        mChildEventListener1 = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                mUsername = snapshot.getValue(User.class).getUserName();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        } ;
//
//        mQueryAnother.addChildEventListener(mChildEventListener1);
        mUsers = new ArrayList<>();

        mGlobalLeaderBoardAdapter = new GlobalLeaderBoardAdapter(this,mUsers);
        mRecyclerView = findViewById(R.id.globall_leaderboard_here);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mGlobalLeaderBoardAdapter);

        findViewById(R.id.change_username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out_menu,menu);;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GlobalLeaderboardActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if(mFirebaseUser.getUid()==user.getUserId())
                {
                    mUsername = user.getUserName();
                    if(mUsername!=null)
                        user.setUserName(mUsername);
                }

                mUsers.add(user);
                Log.i("score",user.getBestScore()+"");
                mGlobalLeaderBoardAdapter.notifyItemInserted(mUsers.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

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
                mQuery.removeEventListener(mChildEventListener);
                mGlobalLeaderBoardAdapter.clear();
                mQuery.addChildEventListener(mChildEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ArrayList<Long> list = new ArrayList<>();
        list = HighScoreSP.getAllScores(this);
        long bestScore = -1* Collections.max(list);
        /*what to do
        * set the value of the score and user_id of that node.
        * If the node doesnt exist it will be created and given a valid user id and score
        * Check if the node has a name in the on child added listener
        *If it doesnt then set the name to the user email*/

        mDatabaseReference.child(mFirebaseUser.getUid()).setValue(new User(mFirebaseUser.getUid(),mUsername,bestScore))
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mQuery.addChildEventListener(mChildEventListener);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        mQuery.removeEventListener(mChildEventListener);
        mGlobalLeaderBoardAdapter.clear();

    }




}
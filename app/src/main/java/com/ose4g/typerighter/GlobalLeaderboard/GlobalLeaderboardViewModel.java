package com.ose4g.typerighter.GlobalLeaderboard;

import android.content.Context;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


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

import com.ose4g.typerighter.Firebase.FireBaseInstance;
import com.ose4g.typerighter.Models.User;
import com.ose4g.typerighter.R;
import com.ose4g.typerighter.SharedPreferences.HighScoreSP;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalLeaderboardViewModel extends ViewModel {

    DatabaseReference mDatabaseReference;
    public Query mQuery;
    public ChildEventListener mChildEventListener;
    public FirebaseUser mFirebaseUser;
    public GoogleSignInOptions mGoogleSignInOptions;
    public GoogleSignInClient mGoogleSignInClient;
    private Context mContext;
    public MutableLiveData<User> currentUser;
    private ArrayList<Long> mList;
    private long mBestScore;
    public MutableLiveData<Boolean> updated;

    public void initializeValues(Context context)
    {
        mContext = context;
        mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.client_id))
                .requestEmail()
                .build();

        mList = new ArrayList<>();
        mList = HighScoreSP.getAllScores(mContext);
        mBestScore = -1* Collections.max(mList);


        mGoogleSignInClient = GoogleSignIn.getClient(mContext,mGoogleSignInOptions);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FireBaseInstance.getDatabaseReference().child("users");
        mQuery = FireBaseInstance.getDatabaseReference().child("users").orderByChild("bestScore");

    }

    public void updateUserScore()
    {
        currentUser = new MutableLiveData<>();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(snapshot.child(mFirebaseUser.getUid()).exists())
                {
                    User value = snapshot.child(mFirebaseUser.getUid()).getValue(User.class);
                    long bestScore = value.getBestScore();

                    if(bestScore>mBestScore)
                    {
                        mDatabaseReference.child(mFirebaseUser.getUid()).child("bestScore").setValue(mBestScore)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            User user= snapshot.child(mFirebaseUser.getUid()).getValue(User.class);
                                            user.setBestScore(mBestScore);
                                            currentUser.postValue(user);

                                        }
                                    }
                                });;
                    }
                    else
                    {
                        currentUser.postValue(value);
                    }
                }

                else
                {
                    final User user = new User(mFirebaseUser.getUid(),mFirebaseUser.getUid().substring(0,4)+(mBestScore*-1),mBestScore);
                    mDatabaseReference.child(mFirebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                currentUser.postValue(user);

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext, mContext.getString(R.string.sign_out_successful), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void updateUsername(String username)
    {
        updated = new MutableLiveData<>();
        mDatabaseReference.child(mFirebaseUser.getUid()).child("userName").setValue(username)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updated.postValue(true);
                    }
                });
    }
}

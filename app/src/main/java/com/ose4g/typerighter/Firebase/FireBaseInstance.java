package com.ose4g.typerighter.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseInstance
{
    private static  DatabaseReference mDatabaseReference;
     public static DatabaseReference getDatabaseReference()
     {
         if(mDatabaseReference==null)
         {
             mDatabaseReference = FirebaseDatabase.getInstance().getReference();
         }
         return mDatabaseReference;
     }
}

package com.example.freephoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FreeList extends AppCompatActivity {
    List<String> list;
    String loggedUsername;
    boolean wantToCloseDialog;
    boolean isRefExist;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_list);
        Intent intent=getIntent();
        loggedUsername=intent.getStringExtra("userName");
        try {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("userlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.getId());
                        }
                        list.remove(loggedUsername);
                    } else {
                        Toast.makeText(FreeList.this, "You don't have any friends in list ...", Toast.LENGTH_SHORT).show();
                    }
                    for (String userName : list) {
                        addFriendToList(userName);
                     }
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addFriendToList(final String userName){
             try {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.friend_list, null);
                Button friendBtn = rowView.findViewById(R.id.friendbtn);
                LinearLayout friendListView=findViewById(R.id.freelist);
                friendListView.addView(rowView, friendListView.getChildCount());
                friendBtn.setText(userName);
                 friendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAlertDialogForAddTravelDetail("Choose Chat Option with "+userName,userName);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void showDialogForLogout(Context context, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.yourDialog);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg).setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent logoutToLogin = new Intent(getApplicationContext(), Login.class);
                        startActivity(logoutToLogin);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        showDialogForLogout(FreeList.this,"Warning","Want to Logout?");
    }
    public void showAlertDialogForAddTravelDetail(String title, final String username) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.yourDialog);
            builder.setTitle(title);
            wantToCloseDialog=false;
             View customLayout = getLayoutInflater().inflate(R.layout.chat_option, null);
            final Button textBtn = customLayout.findViewById(R.id.textbtn);

            LinearLayout scrollOneView=customLayout.findViewById(R.id.chat_layout);
            scrollOneView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent ev)
                {
                    hideKeyboard(view);
                    return false;
                }
            });
            builder.setView(customLayout).setCancelable(false);
            final AlertDialog dialog = builder.create();
            dialog.show();
            textBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        checkReference(loggedUsername+"to"+username);
                        checkReference(username+"to"+loggedUsername);
                        startActivity(new Intent(FreeList.this, TextChat.class)
                                .putExtra("selectedFriend", username).putExtra("loggedUser",loggedUsername));
                        wantToCloseDialog = true;
                        if (wantToCloseDialog)
                            dialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean checkReference(final String ref){
         DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(ref)) {

                }else{
                    textToFrom(ref,"hai,hello");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

             }
        });
        return  isRefExist;
    }
    public void textToFrom(String ref,String msg){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        myRef.setValue(msg);
    }
    public void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
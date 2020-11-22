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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FreeList extends AppCompatActivity {
    List<String> list;
    String username;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_list);
        Intent intent=getIntent();
        username=intent.getStringExtra("userName");
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
                        list.remove(username);
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
    public void addFriendToList(String userName){
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
                        Toast.makeText(FreeList.this, "Function not implemented", Toast.LENGTH_SHORT).show();
                       //showAlertDialogForAddTravelDetail("User Details",email.getText().toString());
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
                        //Utility.clearlogin(getApplicationContext());
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
}
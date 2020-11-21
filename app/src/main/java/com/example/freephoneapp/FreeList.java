package com.example.freephoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FreeList extends AppCompatActivity {
TextView listTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_list);
        listTv=findViewById(R.id.listtv);
        listTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeList.this,Main.class));
            }
        });
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("userlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                 } else {
                    //error
                 }
            }
        });
    }
    public void addFriendToList(ArrayList userList){
             try {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.friend_list, null);
                Button friendBtn = rowView.findViewById(R.id.friendbtn);
                LinearLayout friendListView=findViewById(R.id.list_layout);
                friendListView.addView(rowView, friendListView.getChildCount());
                friendBtn.setText(userList.get(0).toString());
                 rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // showAlertDialogForAddTravelDetail("User Details",email.getText().toString());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
package com.example.freephoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TextChat extends AppCompatActivity {
TextView headerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_chat);
        headerTV=findViewById(R.id.chayheadertv);
        Intent intent=getIntent();
        String friendName=intent.getStringExtra("selectedFriend");
        headerTV.setText("Text Chat With : "+friendName);
    }
}
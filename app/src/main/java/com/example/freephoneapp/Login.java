package com.example.freephoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

 import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Login extends AppCompatActivity {
Button loginBtn;
TextView signUpTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
          loginBtn=findViewById(R.id.loginbtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,FreeList.class));
             }
        });
        signUpTv=findViewById(R.id.signuptv);
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignUp.class));
                //hi
            }
        });
    }
}
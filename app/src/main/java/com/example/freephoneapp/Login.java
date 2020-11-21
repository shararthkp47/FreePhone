package com.example.freephoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

 import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
Button loginBtn;
TextView signUpTv;
    FireBaseTest fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
          loginBtn=findViewById(R.id.loginbtn);
        fb=new FireBaseTest();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fb.addToFirebase("message");
                startActivity(new Intent(Login.this,FreeList.class));
             }
        });
        signUpTv=findViewById(R.id.signuptv);
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Login.this,  fb.retrieveFromFirebase("message"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }
}
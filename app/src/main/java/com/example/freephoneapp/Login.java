package com.example.freephoneapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

 import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {
Button loginBtn;
EditText user,pass;
TextView signUpTv; //hello
boolean isUser;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
          loginBtn=findViewById(R.id.loginbtn);
        user=findViewById(R.id.usernameet);
        pass=findViewById(R.id.passwordet);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
              }
        });
        signUpTv=findViewById(R.id.signuptv);
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(Login.this,SignUp.class));
            }
        });
    }
    public  boolean validate()    {
        try {
            if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                Toast.makeText(Login.this, "please enter username/password", Toast.LENGTH_SHORT).show();
            }else {
                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference docRef = database.collection("userlist").document(user.getText().toString());
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            database.collection("userlist").document(user.getText().toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (task.isComplete()) {
                                            String str_pass = document.get("PASSWORD").toString();
                                            String str_name = document.get("NAME").toString();
                                            if (user.getText().toString().equals(str_name) && pass.getText().toString().equals(str_pass)) {
                                                startActivity(new Intent(Login.this, FreeList.class));
                                            } else {
                                                Toast.makeText(Login.this, "invalid login credentials", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Login.this, "user does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isUser;
    }
}
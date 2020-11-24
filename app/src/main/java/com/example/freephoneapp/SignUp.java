package com.example.freephoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
Button signUpBtn;
TextView loginTv;
EditText userName,setPass,email,phone,conPass;
    boolean isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        try {
            signUpBtn = findViewById(R.id.signupbtn);

            loginTv = findViewById(R.id.logintv);
            userName = findViewById(R.id.usernameet);
            setPass = findViewById(R.id.passwordet);
            email = findViewById(R.id.emailet);
            phone = findViewById(R.id.phoneet);
            conPass = findViewById(R.id.confpasset);

            loginTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUp.this, Login.class));
                }
            });

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userName.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                            || phone.getText().toString().isEmpty() || setPass.getText().toString().isEmpty()
                            || conPass.getText().toString().isEmpty()) {
                        Toast.makeText(SignUp.this, "please enter all fields", Toast.LENGTH_SHORT).show();
                    } else if (!setPass.getText().toString().equals(conPass.getText().toString())) {
                        Toast.makeText(SignUp.this, "password not matching", Toast.LENGTH_SHORT).show();
                    } else if (validation()) {
                        Toast.makeText(SignUp.this, "signed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp.this, "signed un-successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean validation() {
        try {
            // Create a new user with a first, middle, and last name
            Map<String, Object> user = new HashMap<>();
            user.put("NAME", userName.getText().toString());
            user.put("MAIL", email.getText().toString());
            user.put("PHONE", phone.getText().toString());
            user.put("PASSWORD", setPass.getText().toString());
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("userlist").document(userName.getText().toString())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            isUser = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isUser = false;
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
        return isUser;
    }
}
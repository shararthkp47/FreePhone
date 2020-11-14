package com.example.freephoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.net.InetAddress;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isNetworkConnected()){
                    Toast.makeText(SplashScreen.this, "please turn on your internet", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }else if(isInternetAvailable()){
                    Toast.makeText(SplashScreen.this, "your internet is too slow, please try again later", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }else {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                }
            }
        },2000);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("sharath kp");//You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
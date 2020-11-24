package com.example.freephoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextChat extends AppCompatActivity {
TextView headerTV,replayTv,sentTV;
Button sendBtn;
String loggedUser,value,friendName;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
EditText replayEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_chat);

        try {
            headerTV = findViewById(R.id.headertv);
            sendBtn = findViewById(R.id.sendbtn);
            replayTv = findViewById(R.id.replaytv);
            sentTV = findViewById(R.id.senttv);
            replayEt = findViewById(R.id.replayet);
            Intent intent = getIntent();
            friendName = intent.getStringExtra("selectedFriend");
            headerTV.setText("Text Chat With : " + friendName);
             loggedUser=intent.getStringExtra("loggedUser");
             reTextTo(friendName + "to" + loggedUser);
            reTextFrom(loggedUser + "to" + friendName);

            //replayTv.setText(friendName+" : "+replayedMsg);
            //replayEt.setText("you : "+sentMsg);
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textTo(loggedUser + "to" + friendName,replayEt.getText().toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void addNotification(String friendName,String msg) {
        Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/quite_impressed.mp3" ) ;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(TextChat.this,default_notification_channel_id)
                .setSmallIcon(R.drawable.ic_baseline_message_24) //set icon for notification
                .setContentTitle("You Got New Message From "+friendName) //set title of notification
                .setContentText(msg)//this is notification message
                .setSound(sound)
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(NotificationCompat.PRIORITY_MAX) //set priority of notification
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);//to show content in lock screen
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            notificationChannel.setSound(sound , audioAttributes) ;
            builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }

        Intent notificationIntent = new Intent(this, TextChat.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 001, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(001, builder.build());
    }
    public void textTo(String ref,String msg){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        myRef.setValue(msg);
    }
    public String reTextTo(String ref){
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
         myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               String  value = dataSnapshot.getValue(String.class);
                sentTV.setText(friendName+" : "+value);
                addNotification(friendName,value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                 value="---";
             }
        });
        return  value;
    }
    public String reTextFrom(String ref){
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String  value = dataSnapshot.getValue(String.class);
                replayTv.setText("you : "+value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                value="---";
            }
        });
        return  value;
    }
}
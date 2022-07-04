package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Signout extends AppCompatActivity {
private Button signouut;
private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        mauth=FirebaseAuth.getInstance();
        signouut=findViewById(R.id.signoutt);
        signouut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              mauth.signOut();
                Intent intent=new Intent(Signout.this,Signin.class);
                startActivity(intent);
            }
        });
    }
}
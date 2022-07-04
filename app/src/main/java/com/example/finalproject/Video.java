package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Video extends AppCompatActivity {
private EditText name,email,password;
private Button signin;
private TextView already;
private FirebaseUser currentuser;
private FirebaseAuth mauth;
private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
           name=findViewById(R.id.name_signin);
           email=findViewById(R.id.email_signin);
           signin=findViewById(R.id.buttonsignin);
           password=findViewById(R.id.password_signin);
           already=findViewById(R.id.haveanaccount);
           mauth=FirebaseAuth.getInstance();
           loadingbar=new ProgressDialog(this);
           already.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(Video.this,Signin.class);
               startActivity(intent);
           }
       });
       signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
           createnewaccount();
           }
       });
    }

    private void createnewaccount() {
        String namee=name.getText().toString();
        String emaill=email.getText().toString();
        String passwordd=password.getText().toString();
        if(TextUtils.isEmpty(namee))
        {
            Toast.makeText(this,"please enter name..",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(emaill))
        {
            Toast.makeText(this,"please enter email..",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(passwordd))
        {
            Toast.makeText(this,"please enter password..",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingbar.setTitle("Create New Account");
            loadingbar.setMessage("Please Wait,While we are creating new account....");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mauth.createUserWithEmailAndPassword(emaill,passwordd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Sendusertologinactivity();
                            Toast.makeText(Video.this,"Register is sucessfull",Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                        }
                       else
                        {
                            Toast.makeText(Video.this,"Error:"+task.getException().toString(),Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                        }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }

    }
    public void Sendusertologinactivity()
    {
        Intent loginintent=new Intent(Video.this,Signin.class);
        startActivity(loginintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menuu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.men)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("About ");
            alert.setMessage("This app is designed to make it easy for you to build a basic Ardunio robot car ,The app allows you to control an Ardunio based robot car over SplashActivity , and also you can upload Ardunio skitch/code directly from android phone via SplashActivity  ");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.out.println("Ok");
                }
            });
            alert.setNegativeButton("More", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent google=new Intent (Intent.ACTION_VIEW, Uri.parse("www.google.com"));
                    startActivity(google);
                }
            });
            alert.create().show();
        }
        if(item.getItemId()==R.id.feedback)
        {
        }
        if(item.getItemId()==R.id.signout)
        {
            mauth.signOut();
            Intent intent=new Intent(Video.this,Signin.class);
            startActivity(intent);
        }
        return true;
        //bottomnavigation
    }
    void showdialog()
    {
        LayoutInflater inflater=LayoutInflater.from(this);
        View view= inflater.inflate(R.layout.command_alert,null);
        Button okbutton=view.findViewById(R.id.okbtn);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ok");
            }
        });
        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();
    }

}
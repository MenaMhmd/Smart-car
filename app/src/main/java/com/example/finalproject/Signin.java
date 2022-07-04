package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {
private EditText email,pass;
CallbackManager mCallbackManager;
private Button login;

private TextView forgetpassword,newwaccount;
private FirebaseUser currentuser;
private FirebaseAuth mauth;
private ProgressDialog loadingbar;
private final static String TAG="Signin";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = findViewById(R.id.login_button);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                   handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });


        email=findViewById(R.id.emaillogin);
        pass=findViewById(R.id.passwordlogin);
        login=findViewById(R.id.button);
        mauth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);
        forgetpassword=findViewById(R.id.forget);
        currentuser=mauth.getCurrentUser();
        newwaccount=findViewById(R.id.newaccount);
        newwaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sendusertoregisteractivity();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllousertoLogin();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void AllousertoLogin()
    {
        String emaillog=email.getText().toString();
        String passwordlog=pass.getText().toString();
        if(TextUtils.isEmpty(emaillog))
        {
            Toast.makeText(this,"please enter email..",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(passwordlog))
        {
            Toast.makeText(this,"please enter password..",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingbar.setTitle("Sign In");
            loadingbar.setMessage("Please wait.....");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mauth.signInWithEmailAndPassword(emaillog,passwordlog)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Sendusertomainactivity();
                            Toast.makeText(Signin.this,"Login is Sucessfull",Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(Signin.this,"Error:"+task.getException().toString(),Toast.LENGTH_LONG).show();
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
    private void Sendusertomainactivity() {
        Intent intent=new Intent(Signin.this,Detectdevice.class);
        startActivity(intent);
    }
    private void Sendusertoregisteractivity() {
        Intent intent=new Intent(Signin.this,Video.class);
        startActivity(intent);
    }
    private void CreateNewAccount()
    {
        String emaill=email.getText().toString();
        String pas=pass.getText().toString();
        if(TextUtils.isEmpty(emaill))
        {
            Toast.makeText(this,"Please enter your email!",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(pas))
        {
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_LONG).show();
        }
        else
        {

        }
    }
    private void updateUI() {
startActivity(new Intent(Signin.this,Detectdevice.class));
finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mauth.getCurrentUser();
        if(currentUser != null)
        {
        updateUI();
            Sendusertomainactivity();
        }
          }



    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mauth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Signin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }
}
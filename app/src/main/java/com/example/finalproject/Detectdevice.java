package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class Detectdevice extends AppCompatActivity {
    Button btpaired;
    ListView devicelist;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    private BluetoothAdapter mybt=null;
    private Set<BluetoothDevice> paireddevices;
    public static String EXTRAADD="device address";
    String ab=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectdevice);
        TextView tv= (TextView) findViewById(R.id.detecttxt);
        btpaired= (Button) findViewById(R.id.detecbtn);
        mauth=FirebaseAuth.getInstance();
        currentuser=mauth.getCurrentUser();
        devicelist= (ListView) findViewById(R.id.listview);
        mybt=BluetoothAdapter.getDefaultAdapter();
        if(mybt==null)
        {
            Toast.makeText(getApplicationContext(), "SplashActivity devices not available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!mybt.isEnabled())
        {
            Intent turnbton=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnbton,1);
        }
        btpaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paireddeviceslist();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentuser==null)
        {
        Intent intent=new Intent(Detectdevice.this,Signin.class);
        startActivity(intent);
        }
    }

    private void paireddeviceslist(){
        paireddevices = mybt.getBondedDevices();
        ArrayList list = new ArrayList();
        if (paireddevices.size()>0)
        {
            for(BluetoothDevice bt : paireddevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired SplashActivity Devices Found.", Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(mylistclick);
    }
    private AdapterView.OnItemClickListener mylistclick= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent i = new Intent(Detectdevice.this, MainActivity.class);
            i.putExtra(EXTRAADD, address);
            startActivity(i);
        }
    };

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
            showalert();
        }
        if(item.getItemId()==R.id.signout)
        {
            mauth.signOut();
            Intent intent=new Intent(Detectdevice.this,Signin.class);
            startActivity(intent);
        }

        return true;

        //bottomnavigation
    }
    private void showalert()
    {

        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("feedback form");
        alertDialog.setMessage("provide us your valuable feedback");
        LayoutInflater inflater=LayoutInflater.from(this);
        View reg_layout =inflater.inflate(R.layout.send_feedback,null);
        final EditText editTextemail=reg_layout.findViewById(R.id.emaill);
        final EditText editTextname=reg_layout.findViewById(R.id.namee);
        final EditText editTextpassword=reg_layout.findViewById(R.id.passwordd);
        alertDialog.setView(reg_layout);
        alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(editTextemail.getText().toString()))
                {
                    Toast.makeText(Detectdevice.this,"please enter your email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTextname.getText().toString()))
                {
                    Toast.makeText(Detectdevice.this,"please enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTextpassword.getText().toString()))
                {
                    Toast.makeText(Detectdevice.this,"please enter your feedback",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value=snapshot.getValue();
                        ref.child("user").child(editTextname.getText().toString()).child("Email").child(editTextemail.getText().toString());
                        ref.child("user").child(editTextname.getText().toString()).child("Feedback").child(editTextpassword.getText().toString());
                        ref.child("user").child(editTextname.getText().toString()).child("Name").child(editTextname.getText().toString());
                        Toast.makeText(Detectdevice.this,"Thanks for your feedback.....",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Detectdevice.this,"Failed to read value",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}
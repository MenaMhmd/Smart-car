package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.UUID;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    ImageButton st1;
    ImageButton f1;
    ImageButton l1;
    ImageButton r1;
    ImageButton b1;
    Button auto;
    Button manaul;
    private FirebaseAuth mauth;
    private static final byte forward = 'f';
    private static final byte backward = 'b';
    private static final byte right = 'r';
    private static final byte left = 'l';
    private static final byte stop = 's';
    private static final byte outo = 'o';
    private static final byte manual = 'm';
    private static final byte speedd = 'q';
    private static final byte speedd2 = 'k';
    private static final byte speedd3 = 'w';
    private static final byte speedd4 = 'e';
    private static final byte speedd5 = 'z';
    String address = null;
    SeekBar seekbarspeed;
    int pro = 0;
    Runnable runnable;
    ProgressBar progressBar;
    TextView text;
    private ProgressDialog progress;
    BluetoothAdapter myBt = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manaul = findViewById(R.id.manual_id);
        auto = findViewById(R.id.outo_id);
        text = findViewById(R.id.txtnum);
        progressBar = findViewById(R.id.progressbarspeed);
        mauth = FirebaseAuth.getInstance();

        f1 = (ImageButton) findViewById(R.id.uup);
        l1 = (ImageButton) findViewById(R.id.left1);
        b1 = (ImageButton) findViewById(R.id.downn);
        r1 = (ImageButton) findViewById(R.id.righht);
        TextView textView = findViewById(R.id.txt);

        st1 = (ImageButton) findViewById(R.id.stop1);
        seekbarspeed = findViewById(R.id.seekBar);

        seekbarspeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int speed = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //byte b2=(byte)i;
                // Speed(speedd,b2);
                // textView.setText(i);
                //  Toast.makeText(MainActivity.this,i,Toast.LENGTH_LONG).show();
                speed = i;
                if (btSocket != null) {
                    try {
                        if (i == 0) {
                            btSocket.getOutputStream().write("Q".toString().getBytes());
                            pro = 0;
                            updateprogrss();
                        } else if (i == 1) {
                            btSocket.getOutputStream().write("K".toString().getBytes());
                            pro = 25;
                            updateprogrss();
                        } else if (i == 2) {
                            btSocket.getOutputStream().write("W".toString().getBytes());
                            pro = 50;
                            updateprogrss();
                        } else if (i == 3) {
                            btSocket.getOutputStream().write("E".toString().getBytes());
                            pro = 75;
                            updateprogrss();
                        } else if (i == 4) {
                            btSocket.getOutputStream().write("Z".toString().getBytes());
                            pro = 100;
                            updateprogrss();
                        }
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Intent newint = getIntent();
        address = newint.getStringExtra(Detectdevice.EXTRAADD);
        new connectbt().execute();

        manaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manual();

            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auto();
            }
        });
      /*  f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward(forward);
  //          }
//        });*/



        /*l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                left(left);*/
            //}
        //});

        l1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: left(); break;
                    case MotionEvent.ACTION_UP: stop() ;break;
                }

                return true;
            }
        });



        /*r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right(right);
              /*  Intent intent=new Intent(MainActivity.this,MyService.class);
                stopService(intent);*/
           // }
        //});
       f1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: forward(); break;
                    case MotionEvent.ACTION_UP: stop() ;break;
                }

                return true;
            }
        });


        r1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: right(); break;
                    case MotionEvent.ACTION_UP: stop() ;break;
                }

                return true;
            }
        });

       /* b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward(backward);
            }
        });*/

        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: backward(); break;
                    case MotionEvent.ACTION_UP: stop() ;break;
                }

                return true;
            }
        });

                st1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop();
                    }
                });
    }
    private void updateprogrss(){
        progressBar.setProgress(pro);
        text.setText(pro+"%");
    }


    private void disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish(); //return to the first layout

    }

    private void Manual() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("M".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void Auto() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("I".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void forward() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("F".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }

    }

    private void left() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("L".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void right() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("R".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void backward() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("B".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void stop() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("S".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }



    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private class connectbt extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBt = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBt.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP SplashActivity? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menuu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.men) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
                    Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    startActivity(google);
                }
            });
            alert.create().show();
        }
        if (item.getItemId() == R.id.feedback) {
showalert();
        }
        if (item.getItemId() == R.id.signout) {
            mauth.signOut();
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
                    Toast.makeText(MainActivity.this,"please enter your email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTextname.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"please enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTextpassword.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"please enter your Feedback",Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Object value=snapshot.getValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                  Toast.makeText(MainActivity.this,"Failed to read value",Toast.LENGTH_SHORT).show();
                    }
                });
                ref.child("user").child(editTextname.getText().toString()).child("Email").setValue(editTextemail.getText().toString());
                ref.child("user").child(editTextname.getText().toString()).child("Feedback").setValue(editTextpassword.getText().toString());
                ref.child("user").child(editTextname.getText().toString()).child("Name").setValue(editTextname.getText().toString());
                Toast.makeText(MainActivity.this,"Thanks for your feedback.....",Toast.LENGTH_SHORT).show();
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
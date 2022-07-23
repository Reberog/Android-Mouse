package com.example.syncandroidmouse;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import com.example.syncandroidmouse.Accelerometer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    Accelerometer accelerometer;
    public float pos_x = 0.0f;
    public float pos_z = 0.0f;
    public float click = 0.0f;
    public String to_ip = "192.168.137.1";
    float sensitivity = 2.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button lclick = (Button) findViewById(R.id.click);
        Button connect = (Button) findViewById(R.id.connect);
        Button sense = (Button) findViewById(R.id.sense);
        EditText ip = (EditText) findViewById(R.id.ip);
        EditText Sensitivity = (EditText) findViewById(R.id.Sensitivity);

        ip.setText(to_ip);
        Sensitivity.setText(Float.toString(sensitivity));
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ip.getText().toString().equals(""))to_ip = ip.getText().toString();
                Toast.makeText(MainActivity.this, "Connected to "+to_ip, Toast.LENGTH_SHORT).show();
            }
        });

        sense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Sensitivity.getText().toString().equals(""))sensitivity = Float.parseFloat(Sensitivity.getText().toString());
                Toast.makeText(MainActivity.this, "Sensitivity: "+sensitivity, Toast.LENGTH_SHORT).show();
            }
        });

        lclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click = 1.0f;
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Accelerometer accelerometer2 = new Accelerometer(this);
        this.accelerometer = accelerometer2;
        accelerometer2.setListener(new Accelerometer.Listener() {
            public void onTranslation(float tx, float ty, float tz) {
                MainActivity.this.pos_x = (float) Math.floor((double) (sensitivity * tx));
                MainActivity.this.pos_z = (float) Math.floor((double) (sensitivity * tz));
                new Thread(new SenderThread()).start();
            }
        });
    }
    public void onResume() {
        super.onResume();
        this.accelerometer.register();
    }

    public void onPause() {
        super.onPause();
        this.accelerometer.unregister();
    }
    class SenderThread implements Runnable {
        SenderThread() {
        }

        public void run() {
            try {
                Log.d("Connect","connecting");
                Socket s = new Socket(to_ip, 5000);
                Log.d("Connect","connected");
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                Log.d("sending", pos_x+" "+pos_z);
                pw.write(Float.toString(MainActivity.this.pos_x) + " " + Float.toString(MainActivity.this.pos_z)+" "+Float.toString(MainActivity.this.click));
                click=0;
                pw.flush();
                s.close();
            } catch (Exception e) {
                Log.d("err","send");
            }
        }
    }
}
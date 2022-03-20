package com.example.intrack_sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class homepage_intrack extends AppCompatActivity {
    private String id, user;
    TextView real_time, real_date, txt_loggeduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_intrack);

        txt_loggeduser = findViewById(R.id.txt_loggeduser);
        real_date = findViewById(R.id.real_date);
        real_time = findViewById(R.id.real_time);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            user = extras.getString("name");

            txt_loggeduser.setText(user);

            SimpleDateFormat formatdate = new SimpleDateFormat("E, MMM dd yyyy");
            real_date.setText(formatdate.format(new Date()));

            Thread myThread = null;

            Runnable runnable = new realtimeclock();
            myThread= new Thread(runnable);
            myThread.start();

            
        }

    }

    public String getTime(){
        Date dt = new Date();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        String time = String.valueOf(hours)+ ":"+String.valueOf(minutes)+ ":"+ String.valueOf(seconds);
        return time;
    }

    public void runTime()  {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    real_time.setText(getTime());
                }catch (Exception e) {}
            }
        });
    }

    class realtimeclock implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    runTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

}
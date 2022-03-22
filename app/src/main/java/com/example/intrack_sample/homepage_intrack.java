package com.example.intrack_sample;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    Button btn_start, btn_end;
    ConstraintLayout framelayout5;
    DBConnect DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_intrack);

        txt_loggeduser = findViewById(R.id.txt_loggeduser);
        real_date = findViewById(R.id.real_date);
        real_time = findViewById(R.id.real_time);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        framelayout5  = findViewById(R.id.frameLayout5);
        DB = new DBConnect(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get data and display it
            id = extras.getString("id");
            user = extras.getString("name");
            txt_loggeduser.setText(user);
            SimpleDateFormat formatdate = new SimpleDateFormat("E, MMM dd yyyy");
            real_date.setText(formatdate.format(new Date()));

            //run time
            Thread myThread = null;
            Runnable runnable = new realtimeclock();
            myThread= new Thread(runnable);
            myThread.start();
        }

        if(DB.findExistingDate(id, getDate())){
            btn_start.setVisibility(View.INVISIBLE);
        }else{
            btn_end.setVisibility(View.INVISIBLE);
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean time_in = DB.TimeIn(id, getTime(), getDate());

                if(time_in){
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_end.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(homepage_intrack.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private String getTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        String time = String.valueOf(hour)+ ":"+String.valueOf(min)+ ":"+ String.valueOf(sec);
        return time;
    }
    private String getDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int mon = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String date = String.valueOf(day)+ "/"+String.valueOf(mon)+ "/"+ String.valueOf(year);
        return date;

    }

    public void runTime()  {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Calendar cal = Calendar.getInstance();
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int min = cal.get(Calendar.MINUTE);
                    int sec = cal.get(Calendar.SECOND);
                    String hours = String.valueOf(hour).length() == 1 ?  "0" + String.valueOf(hour) : String.valueOf(hour);
                    String minutes = String.valueOf(min).length() == 1 ?  "0" + String.valueOf(min) : String.valueOf(min);
                    String seconds = String.valueOf(sec).length() == 1 ?  "0" + String.valueOf(sec) : String.valueOf(sec);
                    String time = hours + ":"+ minutes+ ":"+ seconds;
                    real_time.setText(time);
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
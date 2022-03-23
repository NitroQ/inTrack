package com.example.intrack_sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
    TextView txt_timeout1, txt_timeout2, txt_timeout3, txt_timeout4, txt_timeout5, txt_timein1, txt_timein2, txt_timein3, txt_timein4, txt_timein5, txt_month1, txt_month2, txt_month3, txt_month4, txt_month5,txt_date1, txt_date2, txt_date3, txt_date4, txt_date5;
    TextView txt_hour1, txt_hour2, txt_hour3, txt_hour4, txt_hour5, workhours;
    Button btn_start, btn_end, btn_logout;
    ConstraintLayout framelayout1, framelayout2, framelayout3, framelayout4, framelayout5;
    DBConnect DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_intrack);

         txt_timeout1 = findViewById(R.id.txt_timeout1); txt_timeout2 = findViewById(R.id.txt_timeout2);txt_timeout3 = findViewById(R.id.txt_timeout3);txt_timeout4= findViewById(R.id.txt_timeout4);txt_timeout5= findViewById(R.id.txt_timeout5);
         txt_timein1= findViewById(R.id.txt_timein1); txt_timein2= findViewById(R.id.txt_timein2); txt_timein3= findViewById(R.id.txt_timein3); txt_timein4= findViewById(R.id.txt_timein4); txt_timein5= findViewById(R.id.txt_timein5);
         txt_month1= findViewById(R.id.txt_month1); txt_month2= findViewById(R.id.txt_month2); txt_month3= findViewById(R.id.txt_month3); txt_month4= findViewById(R.id.txt_month4); txt_month5= findViewById(R.id.txt_month5);
         txt_date1= findViewById(R.id.txt_date1); txt_date2= findViewById(R.id.txt_date2); txt_date3= findViewById(R.id.txt_date3);txt_date4= findViewById(R.id.txt_date4); txt_date5= findViewById(R.id.txt_date5);
         txt_hour1= findViewById(R.id.txt_hours1); txt_hour2= findViewById(R.id.txt_hours2); txt_hour3= findViewById(R.id.txt_hours3); txt_hour4= findViewById(R.id.txt_hours4); txt_hour5= findViewById(R.id.txt_hours5);
        framelayout1  = findViewById(R.id.frameLayout);framelayout2  = findViewById(R.id.frameLayout2);framelayout3  = findViewById(R.id.frameLayout3);framelayout4  = findViewById(R.id.frameLayout4);framelayout5  = findViewById(R.id.frameLayout5);
        txt_loggeduser = findViewById(R.id.txt_loggeduser);
        workhours = findViewById(R.id.workhours);
        real_date = findViewById(R.id.real_date);
        real_time = findViewById(R.id.real_time);
        btn_start = findViewById(R.id.btn_start);
        btn_end = findViewById(R.id.btn_end);
        btn_logout = findViewById(R.id.btn_logout);


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
            displayData();
        }

        if(DB.findExistingDate(id, getDate())){
            btn_start.setVisibility(View.INVISIBLE);
        }else{
            btn_end.setVisibility(View.INVISIBLE);
        }
        if(DB.findExistingTime(id, getDate())){
            btn_end.setVisibility(View.INVISIBLE);
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login1 = new Intent(homepage_intrack.this, login_intrack.class);
                startActivity(login1);
                finish();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add time in data to UserRecord
                Boolean time_in = DB.TimeIn(id, getTime(), getDate());

                //if data was added successfully
                if(time_in){
                    //hide start work and show end work
                    btn_start.setVisibility(View.INVISIBLE);
                    btn_end.setVisibility(View.VISIBLE);
                }else{
                    //if something went wrong:
                    Toast.makeText(homepage_intrack.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
                //update data
                displayData();
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeinval = "";

                //find existing date that was made for time in
                Cursor rs = DB.findTimeIn(id, getDate());

                //if data was found
                if(rs.getCount() != 0){
                    while(rs.moveToNext()){
                        //store it temporarily
                       timeinval = rs.getString(1);
                    }
                }else{
                    Toast.makeText(homepage_intrack.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }


                int hour = 0;
                int min = 0;

                //if timein has value
                if(!timeinval.equals("")) {
                    //convert time to int
                    String[] timein = timeinval.split(":");
                    String[] timeout = getTime().split(":");

                    //calculating total mins/hours passed
                    hour += Integer.parseInt(timeout[0])-Integer.parseInt(timein[0]);
                    int start = Integer.parseInt(timein[1]);
                    int end = Integer.parseInt(timeout[1]);
                    if(start > end){
                        --hour;
                        min += 60;
                    }
                    //round up
                    min += Math.abs(end-start);
                }
                //final calculated time
                String result = String.valueOf((hour*60) + min);

                //update time data with timeout value and add total mins
                Boolean timeoutval = DB.TimeOut(id, getTime(), result, getDate());

                //if successful, show this and hide end work button
                if(timeoutval){
                    Toast.makeText(homepage_intrack.this, "Timed Out!", Toast.LENGTH_SHORT).show();
                    btn_end.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(homepage_intrack.this, "Something went Wrong, Refresh Page!", Toast.LENGTH_SHORT).show();
                }
                // update dashboard data
                displayData();
            }
        });

    }


    public void displayData(){
        //determine month and hiding elements if not needed
        String[] month = {"null", "January", "February", "March","April","May", "June", "July", "August", "September", "October", "November", "December"};
        framelayout1.setVisibility(View.GONE);
        framelayout2.setVisibility(View.GONE);
        framelayout3.setVisibility(View.GONE);
        framelayout4.setVisibility(View.GONE);
        framelayout5.setVisibility(View.GONE);

        //find all the data for that user
        Cursor ps = DB.finduserData(id);

        //initialize values
        int i = 0;
        int mins_total = 0;

        //if data is available
        if(ps.getCount() != 0){
            while(ps.moveToNext()){
                //get total mins and add it
                int mins = ps.getInt(3);
                mins_total += mins;

                // converting time and date to usuable int values
                String[] timeindata = ps.getString(1).split(":");
                String[] datedate = ps.getString(4).split("/");
                String total_hours = String.valueOf(mins/60) ;
                String new_timein = "";
                String new_timeout = "";

                //checking AM/PM
                Boolean halftimein = false;
                Boolean halftimeout = false;

                int hourtimein = Integer.valueOf(timeindata[0]);
                if(hourtimein > 12){
                    hourtimein -= 12;
                    halftimein = true;
                }

                //show time in in 12 hour format
                new_timein = String.valueOf(hourtimein) + ":" + timeindata[1] + ":" + timeindata[2] + (halftimein ? " PM" : " AM");


                //if data is not yet available it will be shown as blank
                if(ps.getString(2) != null){
                    String[] timeoutdata = ps.getString(2).split(":");
                    int hourtimeout = Integer.valueOf(timeoutdata[0]);
                    if(hourtimeout > 12){
                        hourtimeout -= 12;
                        halftimeout = true;
                    }
                    //show time out in 12 hour format
                    new_timeout = String.valueOf(hourtimeout) + ":" + timeoutdata[1] + ":" + timeoutdata[2] + (halftimeout ? " PM" : " AM");
                }

                //display String the month and date
                String dayval = datedate[0];
                String monthval = month[Integer.parseInt(datedate[1])];


                //show the data in Daily time record
                if(i == 0){
                    framelayout1.setVisibility(View.VISIBLE);
                    txt_timein1.setText(new_timein);
                    txt_timeout1.setText(new_timeout);
                    txt_date1.setText(dayval);
                    txt_month1.setText(monthval);
                    txt_hour1.setText(total_hours);
                    i++;
                }else if(i == 1){
                    framelayout2.setVisibility(View.VISIBLE);
                    txt_timein2.setText(new_timein);
                    txt_timeout2.setText(new_timeout);
                    txt_date2.setText(dayval);
                    txt_month2.setText(monthval);
                    txt_hour2.setText(total_hours);
                    i++;
                }else if(i == 2){
                    framelayout3.setVisibility(View.VISIBLE);
                    txt_timein3.setText(new_timein);
                    txt_timeout3.setText(new_timeout);
                    txt_date3.setText(dayval);
                    txt_month3.setText(monthval);
                    txt_hour3.setText(total_hours);
                    i++;
                }else if(i == 3){
                    framelayout4.setVisibility(View.VISIBLE);
                    txt_timein4.setText(new_timein);
                    txt_timeout4.setText(new_timeout);
                    txt_date4.setText(dayval);
                    txt_month4.setText(monthval);
                    txt_hour4.setText(total_hours);
                    i++;
                }else if(i == 4){
                    framelayout5.setVisibility(View.VISIBLE);
                    txt_timein5.setText(new_timein);
                    txt_timeout5.setText(new_timeout);
                    txt_date5.setText(dayval);
                    txt_month5.setText(monthval);
                    txt_hour5.setText(total_hours);
                    i++;
                }

            }
        }

        //calculate the total amount of hours
        String hours_total = String.valueOf(mins_total/60);
        workhours.setText(hours_total + " Hours of Work");

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
                    Boolean halfhour = false;

                    if (hour > 12){
                        hour -= 12;
                        halfhour = true;
                    }

                    String hours = "";
                    if(String.valueOf(hour).length() == 1){
                        hours = "0" + String.valueOf(hour);
                    } else{
                        hours = String.valueOf(hour);
                    }
                    String minutes = String.valueOf(min).length() == 1 ?  "0" + String.valueOf(min) : String.valueOf(min);
                    String seconds = String.valueOf(sec).length() == 1 ?  "0" + String.valueOf(sec) : String.valueOf(sec);
                    String time = hours + ":"+ minutes+ ":"+ seconds + (halfhour ? " PM": " AM");
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
package com.example.intrack_sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class createaccount_intrack extends AppCompatActivity {

    EditText txt_name, txt_name2, txt_username, txt_email, txt_pass, txt_pass2;
    Button btn_create;
    DBConnect DB;


    public void openlogin() {
        Intent login = new Intent(this, login_intrack.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createacc_intrack);

        // This is for setting the frame as fullscreen //
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        txt_name = findViewById(R.id.txt_name);
        txt_name2 = findViewById(R.id.txt_name2);
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);
        txt_pass2 = findViewById(R.id.txt_pass2);
        txt_name = findViewById(R.id.txt_name);
        btn_create = findViewById(R.id.btn_create);
        DB = new DBConnect(this);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = txt_name.getText().toString();
                String lname = txt_name2.getText().toString();
                String uname = txt_username.getText().toString();
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();
                String pass2 = txt_pass2.getText().toString();

               if(fname.trim().equals("") || fname == null){
                   Toast.makeText(createaccount_intrack.this, "Name is Required", Toast.LENGTH_SHORT).show();
               }else if(lname.trim().equals("") || lname == null){
                   Toast.makeText(createaccount_intrack.this, "Name is Required", Toast.LENGTH_SHORT).show();
               }else if(uname.trim().equals("") || uname == null){
                   Toast.makeText(createaccount_intrack.this, "Username is Required", Toast.LENGTH_SHORT).show();
                }else if(email.trim().equals("") || email == null) {
                   Toast.makeText(createaccount_intrack.this, "Email is Required", Toast.LENGTH_SHORT).show();
                }else if(pass.trim().equals("") || pass == null){
                   Toast.makeText(createaccount_intrack.this, "Password is Required", Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(pass2)){
                   Toast.makeText(createaccount_intrack.this, "Passwords Do Not Match.", Toast.LENGTH_SHORT).show();
               }else{
                    Boolean register = DB.register(fname, lname, uname, email, pass);

                    if (register) {
                        Toast.makeText(createaccount_intrack.this, "Account Registered!", Toast.LENGTH_SHORT).show();
                        openlogin();

                    } else {
                        Toast.makeText(createaccount_intrack.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                         txt_name.setText("");
                         txt_name2.setText("");
                         txt_username.setText("");
                         txt_email.setText("");
                         txt_pass.setText("");
                         txt_pass2.setText("");
                    }
                }


            }
        });

    }
}
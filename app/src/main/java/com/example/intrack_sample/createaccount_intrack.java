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

    EditText txt_name, txt_username, txt_email, txt_pass;
    Button btn_create;
    DBConnect DB;


    public void openlogin() {
        Intent login = new Intent(this, login_intrack.class);
        startActivity(login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createacc_intrack);

        // This is for setting the frame as fullscreen //
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        txt_name = findViewById(R.id.txt_name);
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        txt_pass = findViewById(R.id.txt_pass);
        txt_name = findViewById(R.id.txt_name);
        btn_create = findViewById(R.id.btn_create);
        DB = new DBConnect(this);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txt_name.getText().toString();
                String uname = txt_username.getText().toString();
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();

               if(name.trim().equals("") || name == null){
                   Toast.makeText(createaccount_intrack.this, "Name is Required", Toast.LENGTH_SHORT).show();
               }else if(uname.trim().equals("") || uname == null){
                   Toast.makeText(createaccount_intrack.this, "Username is Required", Toast.LENGTH_SHORT).show();
                }else if(email.trim().equals("") || email == null) {
                   Toast.makeText(createaccount_intrack.this, "Email is Required", Toast.LENGTH_SHORT).show();
                }else if(pass.trim().equals("") || pass == null){
                   Toast.makeText(createaccount_intrack.this, "Password is Required", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean register = DB.register(name, uname, email, pass);

                    if (register) {
                        Toast.makeText(createaccount_intrack.this, "Account Registered!", Toast.LENGTH_SHORT).show();
                        openlogin();

                    } else {
                        Toast.makeText(createaccount_intrack.this, "Failed to Register!", Toast.LENGTH_SHORT).show();
                         txt_name.setText("");
                         txt_username.setText("");
                         txt_email.setText("");
                         txt_pass.setText("");
                    }
                }


            }
        });

    }
}
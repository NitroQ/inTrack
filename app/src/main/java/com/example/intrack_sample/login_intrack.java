package com.example.intrack_sample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login_intrack extends AppCompatActivity {

    TextView lbl_createAcc;
    Button btn_Login;
    EditText txt_uname, txt_password;
    DBConnect DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_intrack);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt_uname = findViewById(R.id.txt_uname);
        txt_password = findViewById(R.id.txt_password);
        lbl_createAcc = findViewById(R.id.lbl_createAcc);
        DB = new DBConnect(this);

        lbl_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAcc();
            }
        });

        btn_Login = findViewById(R.id.btn_Login);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_uname = txt_uname.getText().toString();
                String login_pass = txt_password.getText().toString();
                if(login_uname.trim().equals("") || login_uname == null){
                    Toast.makeText(login_intrack.this, "Username is Required", Toast.LENGTH_SHORT).show();
                }else if(login_pass.trim().equals("") || login_pass == null){
                    Toast.makeText(login_intrack.this, "Password is Required", Toast.LENGTH_SHORT).show();
                }else{
                    String id = null, name = null;
                    Cursor res = DB.userLogin(login_uname, login_pass);

                    if (res.getCount() == 0) {
                        Toast.makeText(login_intrack.this, "Wrong Username/Password!", Toast.LENGTH_SHORT).show();
                    }else{
                        while (res.moveToNext()) {
                            id = res.getString(0);
                            name = res.getString(1);
                        }
                       openHome(id, name);
                    }


                }
            }
        });
    }

    public void openCreateAcc() {
        Intent createAcc = new Intent(this, createaccount_intrack.class);
        startActivity(createAcc);
    }

    private void openHome(String id, String name) {
        Intent home = new Intent(this, homepage_intrack.class);
        home.putExtra("id", id);
        home.putExtra("name", name);
        startActivity(home);
    }


}
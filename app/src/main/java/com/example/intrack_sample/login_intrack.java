package com.example.intrack_sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class login_intrack extends AppCompatActivity {

    TextView lbl_createAcc;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_intrack);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lbl_createAcc = findViewById(R.id.lbl_createAcc);

        lbl_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAcc();
            }
        });

        btn_login = findViewById(R.id.btn_Login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }

    public void openCreateAcc() {
        Intent createAcc = new Intent(this, createaccount_intrack.class);
        startActivity(createAcc);
    }

    public void openHome() {
        Intent home = new Intent(this, homepage_intrack.class);
        startActivity(home);
    }


}
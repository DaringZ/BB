package com.devster.bloodybank.Registeration.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.devster.bloodybank.R;

public class LoginActivity extends AppCompatActivity {

    Button btn_googleSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //btn_googleSignIn=(Button) findViewById(R.id.btn_googleSignIn);
    }
}

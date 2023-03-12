package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.autoarticle.R;

public class SignUpActivity extends AppCompatActivity {

    private TextView to_login;

    private EditText register_input_username;

    private EditText register_input_password;

    private EditText register_confirm_input_password;

    private EditText register_input_code;

    private Button register_get_code;
    private Button register_btn;

    private View.OnClickListener toLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initObject();
        initView();
    }

    private void initObject(){
        toLogin=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.this.finish();
            }
        };
    }
    private void initView(){
        to_login=findViewById(R.id.to_login);
        register_input_username=findViewById(R.id.register_input_username);
        register_input_password=findViewById(R.id.register_input_password);
        register_confirm_input_password=findViewById(R.id.register_confirm_input_password);
        register_input_code=findViewById(R.id.register_input_code);
        register_get_code=findViewById(R.id.register_get_code);
        register_btn=findViewById(R.id.register_btn);
        to_login.setOnClickListener(toLogin);
    }
}
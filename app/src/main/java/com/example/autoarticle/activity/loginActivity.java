package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autoarticle.R;

public class loginActivity extends AppCompatActivity {

    private TextView to_register;

    private EditText login_input_username;

    private EditText login_input_password;

    private ImageView passwd_eye_btn;

    private Button login_btn;

    private View.OnClickListener toRegister;
    private View.OnClickListener Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initObject();
        initView();
    }
    private void initObject(){
        toRegister=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        };
        Login=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private void initView(){
        to_register=findViewById(R.id.to_register);
        login_input_username=findViewById(R.id.login_input_username);
        login_input_password=findViewById(R.id.login_input_password);
        passwd_eye_btn=findViewById(R.id.passwd_eye_btn);
        login_btn=findViewById(R.id.login_btn);
        to_register.setOnClickListener(toRegister);
        login_btn.setOnClickListener(Login);
        login_input_username.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] ac = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                return ac;
            }
        });
    }


}
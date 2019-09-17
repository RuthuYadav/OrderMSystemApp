package com.example.ordermanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.emailTextView)
    TextInputEditText userNameEditText;
    @BindView(R.id.password)
    EditText passwordEditText;

    Boolean CheckEditTextEmpty;

    @OnClick(R.id.login_button)
    public void onClick(){
        clickLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    public void CheckEditTextIsEmptyOrNot(String username,String password){

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){

            CheckEditTextEmpty = false ;

        }
        else {
            CheckEditTextEmpty = true ;
        }
    }

    public void clickLogin(){
        CheckEditTextIsEmptyOrNot(userNameEditText.getText().toString(),passwordEditText.getText().toString());
        if(CheckEditTextEmpty == true)
        {
            Toast.makeText(getApplicationContext(),"Logged in successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else {

            Toast.makeText(getApplicationContext(),"Please enter Username and Password", Toast.LENGTH_LONG).show();
        }
    }
}

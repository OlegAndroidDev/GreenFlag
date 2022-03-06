package com.example.greenflag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private EditText emailAddress;
    private EditText passwordCreate;
    private EditText passwordRepeat;
    private TextView passwordMessage;
    private LinearLayout layoutPassword;
    private LinearLayout layoutEmail;
    private Button buttonNext;

    private boolean isEmailValid = false;
    private boolean isEmailValidAndNew =false;
    private boolean isPasswordValid = false;
    private boolean isRepeatPasswordValid = false;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor greenFlagEditor;
    private ImageView button_image_View;

    private boolean isValidEmailAddress(String emailText) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return emailText.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        emailAddress = findViewById(R.id.etEmailAddress);
        passwordCreate = findViewById(R.id.etCreatePassword);
        passwordRepeat = findViewById(R.id.etRepeatPassword);
        passwordMessage = findViewById(R.id.tvPasswordMsg);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutEmail = findViewById(R.id.layoutEmail);
        button_image_View = findViewById(R.id.imgBtnNext);
        buttonNext = findViewById(R.id.btnNext);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutEmail.setVisibility(View.INVISIBLE);
        UpdateActivityLayout();

        sharedPref  = getApplicationContext().getSharedPreferences("SP_GreenFlag", MODE_PRIVATE);
        greenFlagEditor = sharedPref.edit();

        layoutPassword.setVisibility(View.INVISIBLE);
        button_image_View.setEnabled(false);
        buttonNext.setEnabled(false);

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strEmail = emailAddress.getText().toString().trim();
                if(isValidEmailAddress(strEmail))
                {
                    isEmailValid = true;

                    // Here when getting the value from shared preferences is always good practice to return null as default
                    String emailExist = sharedPref.getString(emailAddress.getText().toString(), null);

                    //Log.d("****** Email emailExist", emailExist);

                    // here you check for null instead of "default"
                    if(emailExist == null){
                        //Log.d("****** Email emailExist", "Not found");
                        layoutEmail.setVisibility(View.INVISIBLE);
                        isEmailValidAndNew = true;
                    }
                    else{
                        isEmailValid = false;
                        //Log.d("****** Email emailExist", "Found");
                        layoutEmail.setVisibility(View.VISIBLE);
                        isEmailValidAndNew = false;
                    }
                }
                else{
                    isEmailValid = false;
                }
                UpdateActivityLayout();
            }

        });
        passwordCreate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strPassword = passwordCreate.getText().toString();
                boolean isValidLength = false;
                boolean isNumber = false;
                boolean isUppercase = false;
                boolean isLowerCase = false;

                passwordRepeat.setText("");
                if(passwordCreate.getText().toString().length() >= 8){
                    isValidLength = true;
                    for (char c : strPassword.toCharArray()) {
                        if(Character.isDigit(c)){
                            isNumber = true;
                        }
                        if(Character.isUpperCase(c)){
                            isUppercase = true;
                        }
                        if(Character.isLowerCase(c)){
                            isLowerCase = true;
                        }
                    }
                    if(isValidLength && isNumber && isUppercase && isLowerCase){
                        isPasswordValid = true;
                        passwordCreate.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.tick,0);
                        passwordCreate.setBackground(getDrawable(R.drawable.tv_border_green));
                        layoutPassword.setVisibility(View.INVISIBLE);
                    }
                    else {
                        if(passwordCreate.getText().toString().length() == 0){
                            passwordCreate.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                            layoutPassword.setVisibility(View.INVISIBLE);
                        }
                        else {
                            passwordCreate.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                            //passwordCreate.setBackgroundColor(Color.parseColor("#ffffff"));
                            passwordCreate.setBackground(getDrawable(R.drawable.tv_border));
                            passwordMessage.setText("The password is not valid");
                            //layoutPassword.setBackground(R.color.black);
                            layoutPassword.setVisibility(View.VISIBLE);
                        }
                    }
                }
                UpdateActivityLayout();
            }
        });

        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(isPasswordValid){
                    String strPassword = passwordCreate.getText().toString().trim();
                    String strPasswordRepeat = passwordRepeat.getText().toString().trim();
                    if (strPassword.equals(strPasswordRepeat)) {
                        passwordRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.tick,0);
                        passwordRepeat.setBackground(getDrawable(R.drawable.tv_border_green));
                        layoutPassword.setVisibility(View.INVISIBLE);
                        isRepeatPasswordValid = true;
                    }
                    else{
                        passwordRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        passwordRepeat.setBackground(getDrawable(R.drawable.tv_border));
                        passwordMessage.setText("Your passwords don't match");
                        layoutPassword.setVisibility(View.VISIBLE);
                        isRepeatPasswordValid = false;
                    }
                }
                UpdateActivityLayout();
            }
        });
    }

    public void onClickNext(View view)
    {
        greenFlagEditor.putString(emailAddress.getText().toString(), passwordCreate.getText().toString());
        greenFlagEditor.apply();

        Intent intentAccountCreated = new Intent(getBaseContext(),MainActivity3.class);
        startActivity(intentAccountCreated);
    };
    public void UpdateActivityLayout(){
        if(isEmailValid){

            Log.d("**** Email", "email is valid");
            layoutEmail.setVisibility(View.INVISIBLE);
            emailAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.tick,0);
            emailAddress.setBackground(getDrawable(R.drawable.tv_border_green));
        }else{
            Log.d("**** Email", "email is not valid");
            //layoutEmail.setVisibility(View.VISIBLE);
            button_image_View.setColorFilter(getColor(R.color.green_grey));
            emailAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
            if(emailAddress.getText().toString().trim().length()>0){
                emailAddress.setBackground(getDrawable(R.drawable.border_red_bgr_white));
            }
            else {
                emailAddress.setBackground(getDrawable(R.color.white));
            }
            isEmailValidAndNew = false;
            buttonNext.setEnabled(false);
        }

        if (isEmailValidAndNew && isPasswordValid && isRepeatPasswordValid){
            emailAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.tick,0);
            emailAddress.setBackground(getDrawable(R.drawable.tv_border_green));
            button_image_View.setColorFilter(null);
            buttonNext.setEnabled(true);
        }
        else {
            button_image_View.setColorFilter(getColor(R.color.green_grey));
            buttonNext.setEnabled(false);

        }
    }
}
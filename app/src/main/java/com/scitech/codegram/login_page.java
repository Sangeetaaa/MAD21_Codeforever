package com.scitech.codegram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login_page extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    MaterialButton login;
    TextInputLayout email_field, pass_field;
    ProgressDialog progressDialog;
    FirebaseUser user;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        progressDialog = new ProgressDialog(this);

        register = (TextView) findViewById(R.id.signup_button);
        login = findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();
        email_field = findViewById(R.id.email);
        pass_field = findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_page.this, Signup_page.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });
    }


    @Override
    protected void onStart() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), profile_view.class));
            finish();
        }
        super.onStart();
    }

    private void UserLogin()
    {
        String email, password;

        email = email_field.getEditText().getText().toString();
        password = pass_field.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login_page.this, "Please enter the Email !!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(login_page.this, "Password field cannot be kept blank!!!", Toast.LENGTH_SHORT).show();
        }

        else {

            progressDialog.setMessage("Signing in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                         user = firebaseAuth.getCurrentUser();
                        Toast.makeText(login_page.this, "Login Successful !!!", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                        startActivity(new Intent(login_page.this, profile_view.class));
                    }
                    else {
                        Exception e = task.getException();
                        Toast.makeText(login_page.this, "Login Failed !!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            });
        }
    }
}

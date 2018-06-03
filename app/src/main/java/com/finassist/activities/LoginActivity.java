package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.finassist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * The launcher activity, used for user authentication and creation of new user accounts.
 */
public class LoginActivity extends Activity {
    private static final String LOG_TAG = "LoginActivity";

    private static final int MIN_PASSWORD_LENGTH = 6;

    private FirebaseAuth mAuth;

    private String email = "", password = "", errorText = "";

    private Button btnLogin, btnSignUp;
    private EditText etEmail, etPassword;
    // private TextView tvError;
    private ProgressBar progressBar;

    @SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        // tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progress_bar);

        autoLogin();

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        /*
         btnLogin - try to authenticate the user with given email & password
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showError(getString(R.string.login_empty));
                    return;
                }

                if (password.length() < MIN_PASSWORD_LENGTH) {
                    showError(String.format(getString(R.string.login_passshort), MIN_PASSWORD_LENGTH));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) { // there was an error
                                    showError(getString(R.string.login_authfailed));
                                } else { // start main activity
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });


        /*
         btnSignUp - try to create a new account with given email & password + authenticate the user
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                // validate email and password fields

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showError(getString(R.string.login_empty));
                    return;
                }

                if (password.length() < MIN_PASSWORD_LENGTH) {
                    showError(String.format(getString(R.string.login_passshort), MIN_PASSWORD_LENGTH));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) { // there was an error
                                    showError(getString(R.string.login_signupfailed));
                                } else { // inform user of success, start main activity
                                    Toast.makeText(LoginActivity.this, getString(R.string.login_signupsuccess), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });

    }

    private void autoLogin() {
        etEmail.setText("jasminvucelj1@gmail.com"); // TEST
        etPassword.setText("jasv49"); // TEST
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", etEmail.getText().toString());
        outState.putString("password", etPassword.getText().toString());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etEmail.setText(savedInstanceState.getString("email"));
        etPassword.setText(savedInstanceState.getString("password"));
    }


    private void showError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }

}

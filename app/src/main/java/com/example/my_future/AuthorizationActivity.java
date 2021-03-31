package com.example.my_future;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_SAVE_SETTINGS;
import static com.example.my_future.Variables.MY_AUTH;

public class AuthorizationActivity extends AppCompatActivity {
    EditText email, password, emailResetPassword;
    Button come;
    ProgressBar progressBar;

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    SharedPreferences checkDataSettings, saveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        init();
    }

    private void init() {
        email = findViewById(R.id.email_authorization);
        password = findViewById(R.id.password_authorization);
        come = findViewById(R.id.comeButton);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        checkDataSettings = getSharedPreferences(ALL_CHECK_DATA, MODE_PRIVATE);
        saveSettings = getSharedPreferences(ALL_SAVE_SETTINGS, Context.MODE_PRIVATE);

        email.setText("veretennik-v@mail.ru");
        password.setText("121212");
//        email.setText(getIntent().getStringExtra("email"));
//        password.setText(getIntent().getStringExtra("password"));
    }

    private void MyToast(String message) {
        Toast.makeText(AuthorizationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            comeEmailVer();
        }
    }

    public void onClickAuthorization(View view) {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();
        if (email_text.isEmpty()) {
            MyToast("Поле email пустое!");
            return;
        }
        if (password_text.isEmpty()) {
            MyToast("Поле пароль пустое!");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        come.setClickable(false);
        mAuth.signInWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        comeEmailVer();
                        myRef.child(mAuth.getUid()).child("password").setValue(password_text);
                    } else {
                        MyToast("Авторизация провалена");
                        progressBar.setVisibility(View.GONE);
                        come.setClickable(true);
                    }
                });
    }

    public void comeEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            if (!checkDataSettings.getString(MY_AUTH, "").equals(String.valueOf(mAuth.getUid()))) {
                checkDataSettings.edit().clear().apply();
                saveSettings.edit().clear().apply();
                SharedPreferences.Editor editorCheck = checkDataSettings.edit();
                editorCheck.putString(MY_AUTH, String.valueOf(mAuth.getUid()));
                editorCheck.apply();
            }
            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else {
            MyToast("Зайди на почту");
            progressBar.setVisibility(View.GONE);
        }
        come.setClickable(true);
    }

    public void onClickRegistrationActivity(View view) {
        startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
    }

    public void onClickResetPassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_MyAlertDialog);
        LayoutInflater layoutInflater = getLayoutInflater();
        View viewAlert = layoutInflater.inflate(R.layout.alert_dialog_reset_password, null);
        builder.setView(viewAlert).setCancelable(true);
        AlertDialog alertDialog = builder.create();

        emailResetPassword = viewAlert.findViewById(R.id.email);
        Button resetPassword = viewAlert.findViewById(R.id.butResetPassword);
        ProgressBar progressBarReset = viewAlert.findViewById(R.id.progressBar);
        resetPassword.setOnClickListener(v -> {
            String email_text = emailResetPassword.getText().toString();
            if (email_text.isEmpty()) {
                MyToast("Введите логин");
                return;
            }
            mAuth.sendPasswordResetEmail(email_text).addOnCompleteListener(task -> {
                progressBarReset.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    MyToast("Зайдите на почту");
                    alertDialog.dismiss();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                        MyToast("Неверный адрес");
                    } catch (Exception e) {
                        MyToast(e.getMessage());
                    } finally {
                        progressBarReset.setVisibility(View.GONE);
                    }
                }
            });
        });
        alertDialog.show();
    }
}
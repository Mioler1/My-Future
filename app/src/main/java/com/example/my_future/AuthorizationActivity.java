package com.example.my_future;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorizationActivity extends AppCompatActivity {
    EditText email, password, emailResetPassword;
    Button come;
    ProgressBar progressBar;

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        init();
    }

    private void init() {
        email = findViewById(R.id.email_authorization);
        password = findViewById(R.id.password_authorization);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        email.setText(getIntent().getStringExtra("email"));
//        password.setText(getIntent().getStringExtra("password"));
        email.setText("veretennik-v@mail.ru");
        password.setText("123123");

        come = findViewById(R.id.comeButton);
        come.setOnClickListener(view -> Authorization());
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

    private void Authorization() {
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

        mAuth.signInWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()) {
                        comeEmailVer();
                        myRef.child(mAuth.getUid()).child("password").setValue(password_text);
                    } else {
                        MyToast("Авторизация провалена");
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void comeEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else {
            MyToast("Зайди на почту");
            progressBar.setVisibility(View.GONE);
        }
    }

    public void onClickRegistrationActivity(View view) {
        startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
    }

    public void onClickResetPassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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
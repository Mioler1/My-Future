package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorizationActivity extends AppCompatActivity {
    EditText email, password;
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

        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));
        password.setText(intent.getStringExtra("password"));

        come = findViewById(R.id.comeButton);
        come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authorization();
            }
        });

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
        if (email.getText().toString().isEmpty()) {
            MyToast("Поле email пустое!");
            return;
        }
        if (password.getText().toString().isEmpty()) {
            MyToast("Поле пароль пустое!");
            return;
        }

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            comeEmailVer();
                        } else {
                            MyToast("Авторизация провалена");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void comeEmailVer() {
        assert mUser != null;
        if (mUser.isEmailVerified()) {
            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else {
            MyToast("Зайди на почту");
            progressBar.setVisibility(View.GONE);
        }
    }

    public void onClickRegistrationActivity(View view) {
        startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
    }

    public void onClickChangePassword(View view) {
        mAuth.sendPasswordResetEmail("veretennik-v@mail.ru");
    }
}
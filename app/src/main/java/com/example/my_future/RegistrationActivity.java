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

import com.example.my_future.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText email, password, repeatPassword;
    Button registration;
    ProgressBar progressBar;

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        registration = findViewById(R.id.regButton);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void Registration() {
        email = findViewById(R.id.email_registration);
        password = findViewById(R.id.password_registration);
        repeatPassword = findViewById(R.id.repeat_password_registration);

        if (email.getText().toString().isEmpty()) {
            MyToast("Поле email пустое!");
            return;
        }
        if (password.getText().toString().isEmpty()) {
            MyToast("Поле пароль пустое!");
            return;
        }
        if (password.getText().toString().length() < 6) {
            MyToast("Поле пароль должно содержать не менее 6 символов!");
            return;
        }
        if (password.getText().toString().isEmpty()) {
            MyToast("Поле повторный пароль пустое!");
            return;
        }
        if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
            MyToast("Повторный пароль введен неверно!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());
                            myRef.child(mAuth.getUid()).setValue(user);
                            myRef.child(mAuth.getUid()).child("profile").setValue("none");
                            sendEmailVer();
                        } else {
                            MyToast("Регистрация провалена");
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void sendEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MyToast("Зайди на почту");
                    startActivity();
                    finish();
                } else {
                    MyToast("Не работает");
                }
            }
        });
    }

    private void startActivity() {
        Intent intent = new Intent(RegistrationActivity.this, AuthorizationActivity.class);
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("password", password.getText().toString());
        startActivity(intent);
    }
}
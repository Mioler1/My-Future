package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.my_future.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthorizationActivity extends AppCompatActivity {
    EditText email, password, repeatPassword;
    Button come, registration;

    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        init();
    }

    private void init() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        come = findViewById(R.id.comeButton);
        registration = findViewById(R.id.regButton);

        come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authorization();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });
    }

    public void onClickAuthorization(View view) {
        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
        authorizationLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);

        EditText emailAf = findViewById(R.id.email_authorization);
        EditText emailReg = findViewById(R.id.email_registration);
        EditText passwordAf = findViewById(R.id.password_authorization);
        EditText passwordReg = findViewById(R.id.password_registration);

        emailAf.setText(emailReg.getText().toString());
        passwordAf.setText(passwordReg.getText().toString());
    }

    public void onClickRegistration(View view) {
        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
        authorizationLayout.setVisibility(View.GONE);
        registrationLayout.setVisibility(View.VISIBLE);
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
        email = findViewById(R.id.email_authorization);
        password = findViewById(R.id.password_authorization);

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
                        if (task.isSuccessful()) {
                            comeEmailVer();
                        } else {
                            MyToast("Авторизация провалена");
                        }
                    }
                });
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
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());
                            myRef.child(mAuth.getUid()).setValue(user);
                            sendEmailVer();
                        } else {
                            MyToast("Авторизация провалена");
                        }
                    }
                });
    }

    public void comeEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class));
            finish();
        } else {
            MyToast("Зайди на почту");
        }
    }

    private void sendEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MyToast("Зайди на почту");
                } else {
                    MyToast("Не работает");
                }
            }
        });
    }
}
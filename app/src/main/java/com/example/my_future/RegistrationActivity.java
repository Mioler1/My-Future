package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.my_future.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
        registration = findViewById(R.id.regButton);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    private void MyToast(String message) {
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickRegistration(View view) {
        email = findViewById(R.id.email_registration);
        password = findViewById(R.id.password_registration);
        repeatPassword = findViewById(R.id.repeat_password_registration);
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();
        String repeat_password_text = repeatPassword.getText().toString();
        boolean check = false;
        if (email_text.isEmpty()) {
            MyToast("Поле email пустое!");
            return;
        }
        if (password_text.isEmpty()) {
            MyToast("Поле пароль пустое!");
            return;
        }
        if (password_text.length() < 6) {
            MyToast("Поле пароль должно содержать не менее 6 символов!");
            return;
        }
        if (password_text.matches("[a-zA-Zа-яА-Я0-9@$#?&_.-]+")) {
            check = true;
        }
        if (!check) {
            MyToast("Некоректный пароль");
            return;
        }
        if (email_text.equals(password_text)) {
            MyToast("Логин не должен совпадать с паролем");
            return;
        }
        if (repeat_password_text.isEmpty()) {
            MyToast("Поле повторный пароль пустое!");
            return;
        }
        if (!password_text.equals(repeat_password_text)) {
            MyToast("Повторный пароль введен неверно!");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        registration.setClickable(false);
        mAuth.createUserWithEmailAndPassword(email_text, password_text)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = new User();
                        user.setEmail(email_text);
                        user.setPassword(password_text);
                        myRef.child(mAuth.getUid()).setValue(user);
                        myRef.child(mAuth.getUid()).child("profile").setValue("none");
                        myRef.child(mAuth.getUid()).child("health").setValue("none");
                        myRef.child(mAuth.getUid()).child("volume").setValue("none");
                        myRef.child(mAuth.getUid()).child("Administration").child("ban").setValue("false");
                        myRef.child(mAuth.getUid()).child("Administration").child("role").setValue("user");
                        myRef.child(mAuth.getUid()).child("Administration").child("subscription").setValue("false");
                        sendEmailVer();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        registration.setClickable(true);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                            MyToast("Неверный адрес");
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            MyToast("Данный email уже используется");
                        } catch (Exception e) {
                            MyToast(e.getMessage());
                        }
                    }
                });
    }

    private void sendEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                MyToast("Зайди на почту");
                startActivity();
                finish();
            } else {
                progressBar.setVisibility(View.GONE);
                registration.setClickable(true);
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
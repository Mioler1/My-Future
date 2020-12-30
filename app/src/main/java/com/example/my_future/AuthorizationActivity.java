package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.my_future.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private void Authorization() {
        email = findViewById(R.id.email_authorization);
        password = findViewById(R.id.password_authorization);
    }

    private void Registration() {
        email = findViewById(R.id.email_registration);
        password = findViewById(R.id.password_registration);
        repeatPassword = findViewById(R.id.repeat_password_registration);

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());

                        myRef.child(mAuth.getUid()).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        MyToast("Авторизация завершена");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MyToast("Авторизация провалена");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MyToast("Пользователь уже существует");
                    }
                });
    }

    public void onClickAuthorization(View view) {
        LinearLayout authorizationLayout = findViewById(R.id.AutoLayout);
        LinearLayout registrationLayout = findViewById(R.id.RegLayout);
        authorizationLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);
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
}
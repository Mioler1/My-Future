package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthorizationActivity extends AppCompatActivity {
    EditText email, password, repeatPassword;
    Button come, registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        init();
    }

    private void init() {
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
        email = findViewById(R.id.email_authorization);
        password = findViewById(R.id.password_authorization);
        repeatPassword = findViewById(R.id.repeat_password_registration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyToast("Успешно");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Провал");
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
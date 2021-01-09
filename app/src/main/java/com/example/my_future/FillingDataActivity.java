package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_future.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FillingDataActivity extends AppCompatActivity {

    EditText nickname, weight;
    RadioGroup gender;
    Spinner target;
    TextView textNoVisibleGender;
    TextView textNoVisibleTarget;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data);

        init();
        genderSelection();
        targetSelection();
    }

    private void init() {
        nickname = findViewById(R.id.nickname);
        weight = findViewById(R.id.weight);
        gender = findViewById(R.id.gender);
        target = findViewById(R.id.target);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClickSaveFillingData(View view) {
        if (nickname.getText().toString().isEmpty()) {
            MyToast("Поле никнейм пустое");
            return;
        }
        if (weight.getText().toString().isEmpty()) {
            MyToast("Поле вес пустое");
            return;
        }
        if (textNoVisibleGender.getText().toString().isEmpty()) {
            MyToast("Выберите пол");
            return;
        }
        if (textNoVisibleTarget.getText().toString().equals("Выберите цель")) {
            MyToast("Выберите цель");
            return;
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                User user = new User();
                user.setNickname(nickname.getText().toString());
                user.setWeight(weight.getText().toString());
                user.setGender(textNoVisibleGender.getText().toString());
                user.setTarget(textNoVisibleTarget.getText().toString());
                myRef.child(mAuth.getUid()).child("profile").setValue(user);

                comeEmailVer();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    private void genderSelection() {
        gender.clearCheck();
        textNoVisibleGender = findViewById(R.id.visible_text_gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.gender_men:
                        textNoVisibleGender.setText("Мужской");
                        break;
                    case R.id.gender_girl:
                        textNoVisibleGender.setText("Женский");
                        break;
                }
            }
        });
    }

    private void targetSelection() {
        textNoVisibleTarget = findViewById(R.id.visible_text_target);
        String[] targets = {"Выберите цель", "Похудеть", "Рельеф", "Мышечная масса", "Сила"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, targets) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleTarget.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        target.setOnItemSelectedListener(itemSelectedListener);
    }

    public void comeEmailVer() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            startActivity(new Intent(FillingDataActivity.this, MainActivity.class));
        } else {
            MyToast("Зайди на почту");
            startActivity(new Intent(FillingDataActivity.this, AuthorizationActivity.class));
        }
        finish();
    }
}
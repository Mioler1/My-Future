package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_future.Adapters.PressureAdapter;
import com.example.my_future.Models.Pressure;
import com.example.my_future.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_DISEASES;
import static com.example.my_future.Variables.APP_PREFERENCES_EXPERIENCE;
import static com.example.my_future.Variables.APP_PREFERENCES_PRESSURE;

public class FillingDataHealthActivity extends AppCompatActivity {

    // Окно с данными о здоровье пользователя
    TextView textNoVisiblePressure, textNoVisibleDiseases, textNoVisibleExperience;
    Spinner diseases, experience;
    RecyclerView recyclerPressure;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data_health);

        init();
        pressureSelection();
        diseasesSelection();
        experienceSelection();
    }

    private void init() {
        mSettings = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        // Окно с данными о здоровье пользователя
        recyclerPressure = findViewById(R.id.pressureList);
        diseases = findViewById(R.id.diseases);
        experience = findViewById(R.id.experience);
    }

    public void onClickSaveDataHealth(View view) {
        String text_pressure = textNoVisiblePressure.getText().toString();
        String text_diseases = textNoVisibleDiseases.getText().toString();
        String text_experience = textNoVisibleExperience.getText().toString();
        if (text_pressure.isEmpty()) {
            MyToast("Выберите давление");
            return;
        }
        if (text_diseases.equals("Выберите заболевание")) {
            MyToast("Выберите заболевание");
            return;
        }
        if (text_experience.equals("Выберите стаж")) {
            MyToast("Выберите стаж");
            return;
        }
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user.setPressure(text_pressure);
                user.setDiseases(text_diseases);
                user.setExperience(text_experience);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_PRESSURE, text_pressure);
                editor.putString(APP_PREFERENCES_DISEASES, text_diseases);
                editor.putString(APP_PREFERENCES_EXPERIENCE, text_experience);

                editor.apply();
                myRef.child(mAuth.getUid()).child("health").setValue(user);
                myRef.child(mAuth.getUid()).child("health").child("activity").setValue("none");

                startActivity(new Intent(FillingDataHealthActivity.this, FillingDataActivismActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataHealthActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void pressureSelection() {
        textNoVisiblePressure = findViewById(R.id.visible_text_pressure);

        recyclerPressure.setLayoutManager(new LinearLayoutManager(this));
        recyclerPressure.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ArrayList<Pressure> pressureArrayList = new ArrayList<>();
        pressureArrayList.add(new Pressure("Гипотания", "<100", "<60"));
        pressureArrayList.add(new Pressure("Оптимальное", "<120", "<80"));
        pressureArrayList.add(new Pressure("Нормальное", "<130", "<85"));
        pressureArrayList.add(new Pressure("Повышенное", "130-139", "85-89"));
        pressureArrayList.add(new Pressure("Гипертония легкая", "140-159", "90-99"));
        pressureArrayList.add(new Pressure("Гипертония умеренная", "160-179", "100-109"));
        pressureArrayList.add(new Pressure("Гипертония тяжёлая", "≥180", "≥110"));

        PressureAdapter.OnUserClickListener onUserClickListener = pressure -> textNoVisiblePressure.setText(pressure.getNamePressure());

        PressureAdapter pressureAdapter = new PressureAdapter(this, pressureArrayList, onUserClickListener);
        recyclerPressure.setAdapter(pressureAdapter);
    }

    private void diseasesSelection() {
        textNoVisibleDiseases = findViewById(R.id.visible_text_diseases);
        String[] diseasesS = getResources().getStringArray(R.array.diseases);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, diseasesS) {
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
        diseases.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleDiseases.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        diseases.setOnItemSelectedListener(itemSelectedListener);
    }

    private void experienceSelection() {
        textNoVisibleExperience = findViewById(R.id.visible_text_experience);
        String[] experiences = getResources().getStringArray(R.array.experience);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, experiences) {
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
        experience.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleExperience.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        experience.setOnItemSelectedListener(itemSelectedListener);
    }
}
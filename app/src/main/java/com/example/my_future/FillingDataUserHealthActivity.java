package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_future.Intro.IntroActivity;
import com.example.my_future.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_DISEASES;
import static com.example.my_future.Variables.APP_PREFERENCES_EXPERIENCE;
import static com.example.my_future.Variables.APP_PREFERENCES_PRESSURE;

public class FillingDataUserHealthActivity extends AppCompatActivity {

    // Окно с данными о здоровье пользователя
    TextView textNoVisiblePressure, textNoVisibleDiseases, textNoVisibleExperience;
    Spinner pressure, diseases, experience;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    StorageReference mStorageRef;
    SharedPreferences mSettings;

    RelativeLayout relativeDataHealth;
    RelativeLayout relativeDataActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data2);

        relativeDataHealth = findViewById(R.id.AutoLayout3);
        relativeDataActivity = findViewById(R.id.AutoLayout4);
        init();
        pressureSelection();
        diseasesSelection();
        experienceSelection();
        onClickSaveDataActivity();
    }

    private void init() {
        FillingDataUserHealthActivity fillingDataUserHealthActivityClass = FillingDataUserHealthActivity.this;
        mSettings = fillingDataUserHealthActivityClass.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");

        // Окно с данными о здоровье пользователя
        pressure = findViewById(R.id.pressure);
        diseases = findViewById(R.id.diseases);
        experience = findViewById(R.id.experience);
    }

    public void onClickSaveDataHealth(View view) {
        String text_pressure = textNoVisiblePressure.getText().toString();
        String text_diseases = textNoVisibleDiseases.getText().toString();
        String text_experience = textNoVisibleExperience.getText().toString();
        if (text_pressure.equals("Выберите давление")) {
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

                relativeDataHealth.setVisibility(View.GONE);
                relativeDataActivity.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    public void onClickSaveDataActivity() {
        TextView textNoVisibleActivity = findViewById(R.id.visible_text_activity);
        ImageButton but_activity_1 = findViewById(R.id.image_click_activity_1);
        ImageButton but_activity_2 = findViewById(R.id.image_click_activity_2);
        ImageButton but_activity_3 = findViewById(R.id.image_click_activity_3);
        ImageButton but_activity_4 = findViewById(R.id.image_click_activity_4);
        ImageButton but_activity_5 = findViewById(R.id.image_click_activity_5);
        Button next_activity = findViewById(R.id.but_activity_nextClick);

        but_activity_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textNoVisibleActivity.setText("1.2");
            }
        });
        but_activity_2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textNoVisibleActivity.setText("1.375");
            }
        });
        but_activity_3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textNoVisibleActivity.setText("1.55");
            }
        });
        but_activity_4.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textNoVisibleActivity.setText("1.725");
            }
        });
        but_activity_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textNoVisibleActivity.setText("1.9");
            }
        });

        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_activity = textNoVisibleActivity.getText().toString();
                if (text_activity.isEmpty()) {
                    MyToast("Выберите свою активность");
                    return;
                }
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_PRESSURE, text_activity);
                editor.apply();

                myRef.child(mAuth.getUid()).child("health").child("activity").setValue(text_activity);
                startActivity(new Intent(FillingDataUserHealthActivity.this, IntroActivity.class));
                finish();
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataUserHealthActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void pressureSelection() {
        textNoVisiblePressure = findViewById(R.id.visible_text_pressure);
        String[] pressures = {"Выберите давление", "Гепотания", "Оптимальное", "Повышенное", "Гепертония легкая", "Гепертония умеренная", "Гепертония тяжёлая"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pressures) {
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
        pressure.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisiblePressure.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        pressure.setOnItemSelectedListener(itemSelectedListener);
    }

    private void diseasesSelection() {
        textNoVisibleDiseases = findViewById(R.id.visible_text_diseases);
        String[] diseasesS = {"Выберите заболевание", "Нет заболеваний"};

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
        String[] experiences = {"Выберите стаж", "Новичок", "Любитель", "Продвинутый", "Профи"};

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

package com.example.my_future.CalculetedActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_future.R;

public class CalculatedKetchActivity extends AppCompatActivity {
    TextView text_gender_noVisible, text_activity_noVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculated_ketch);

        genderSelected();
        activitySelected();
        dataWrite();
    }

    private void genderSelected() {
        text_gender_noVisible = findViewById(R.id.visible_text_gender);
        ImageButton but_man = findViewById(R.id.image_click_man);
        ImageButton but_girl = findViewById(R.id.image_click_girl);
        Button next_gender = findViewById(R.id.but_gender_nextClick);

        but_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_gender_noVisible.setText("Мужской");
                Toast.makeText(CalculatedKetchActivity.this, "Мужской", Toast.LENGTH_SHORT).show();
            }
        });
        but_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_gender_noVisible.setText("Женский");
                Toast.makeText(CalculatedKetchActivity.this, "Женский", Toast.LENGTH_SHORT).show();
            }
        });
        next_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_gender_noVisible.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatedKetchActivity.this, "Выберите пол", Toast.LENGTH_SHORT).show();
                    return;
                }
                RelativeLayout relativeGender = findViewById(R.id.genderRel);
                relativeGender.setVisibility(View.GONE);
                RelativeLayout relativeActivity = findViewById(R.id.activityRel);
                relativeActivity.setVisibility(View.VISIBLE);
            }
        });
    }

    private void activitySelected() {
        text_activity_noVisible = findViewById(R.id.visible_text_activity);
        ImageButton but_activity_1 = findViewById(R.id.image_click_activity_1);
        ImageButton but_activity_2 = findViewById(R.id.image_click_activity_2);
        ImageButton but_activity_3 = findViewById(R.id.image_click_activity_3);
        ImageButton but_activity_4 = findViewById(R.id.image_click_activity_4);
        ImageButton but_activity_5 = findViewById(R.id.image_click_activity_5);
        Button next_activity = findViewById(R.id.but_activity_nextClick);

        but_activity_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.2");
            }
        });
        but_activity_2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.375");
            }
        });
        but_activity_3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.55");
            }
        });
        but_activity_4.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.725");
            }
        });
        but_activity_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.9");
            }
        });
        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_activity_noVisible.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatedKetchActivity.this, "Выберите свою активность", Toast.LENGTH_SHORT).show();
                    return;
                }

                RelativeLayout relativeActivity = findViewById(R.id.activityRel);
                relativeActivity.setVisibility(View.GONE);
                RelativeLayout relativeData = findViewById(R.id.dataRel);
                relativeData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void dataWrite() {
        EditText age_text = findViewById(R.id.age);
        EditText growth_text = findViewById(R.id.growth);
        EditText weight_text = findViewById(R.id.weight);
        Button calculate = findViewById(R.id.but_data_nextClick);

        calculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (age_text.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatedKetchActivity.this, "Введите свой возраст", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (growth_text.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatedKetchActivity.this, "Введите свой рост", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (weight_text.getText().toString().isEmpty()) {
                    Toast.makeText(CalculatedKetchActivity.this, "Введите свой вес", Toast.LENGTH_SHORT).show();
                    return;
                }

                RelativeLayout relativeData = findViewById(R.id.dataRel);
                relativeData.setVisibility(View.GONE);
                RelativeLayout relativeResult = findViewById(R.id.resultRel);
                relativeResult.setVisibility(View.VISIBLE);
                TextView result_text = findViewById(R.id.result);

                double activity = Double.parseDouble(text_activity_noVisible.getText().toString());
                double weight = Double.parseDouble(weight_text.getText().toString());
                int growth = Integer.parseInt(growth_text.getText().toString());
                int age = Integer.parseInt(age_text.getText().toString());

                if (text_gender_noVisible.getText().toString().equals("Мужской")) {
                    double imt = weight / (growth * growth);
                    double fat = (1.2 * imt) + (0.23 * age) - (10.8 * 1) - 5.4;
                    double lbm = (weight * (100 - fat)) / 100;
                    double bmr = 370 + (21.6 * lbm) * activity;

                    result_text.setText(bmr + " калорий");
                }
                if (text_gender_noVisible.getText().toString().equals("Женский")) {
                    double imt = weight / (growth * growth);
                    double fat = (1.2 * imt) + (0.23 * age) - (10.8 * 0) - 5.4;
                    double lbm = (weight * (100 - fat)) / 100;
                    double bmr = 370 + (21.6 * lbm) * activity;

                    result_text.setText(bmr + " калорий");
                }
            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}
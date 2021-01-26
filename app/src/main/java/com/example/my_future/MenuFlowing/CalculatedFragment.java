package com.example.my_future.MenuFlowing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.R;

public class CalculatedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_avtivity_calculated, container, false);
        //  Страница выбора пола
        TextView text_gender_noVisible = v.findViewById(R.id.visible_text_gender);
        ImageButton but_man = v.findViewById(R.id.image_click_man);
        ImageButton but_girl = v.findViewById(R.id.image_click_girl);
        Button next_gender = v.findViewById(R.id.but_gender_nextClick);

        but_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_gender_noVisible.setText("Мужской");
                Toast.makeText(getContext(), "Мужской", Toast.LENGTH_SHORT).show();
            }
        });
        but_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_gender_noVisible.setText("Женский");
                Toast.makeText(getContext(), "Женский", Toast.LENGTH_SHORT).show();
            }
        });
        next_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_gender_noVisible.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Выберите пол", Toast.LENGTH_SHORT).show();
                    return;
                }
                RelativeLayout relativeGender = v.findViewById(R.id.genderRel);
                relativeGender.setVisibility(View.GONE);
                RelativeLayout relativeActivity = v.findViewById(R.id.activityRel);
                relativeActivity.setVisibility(View.VISIBLE);
            }
        });
        // Страница выбора активности
        TextView text_activity_noVisible = v.findViewById(R.id.visible_text_activity);
        ImageButton but_activity_1 = v.findViewById(R.id.image_click_activity_1);
        ImageButton but_activity_2 = v.findViewById(R.id.image_click_activity_2);
        ImageButton but_activity_3 = v.findViewById(R.id.image_click_activity_3);
        ImageButton but_activity_4 = v.findViewById(R.id.image_click_activity_4);
        ImageButton but_activity_5 = v.findViewById(R.id.image_click_activity_5);
        Button next_activity = v.findViewById(R.id.but_activity_nextClick);

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
                    Toast.makeText(getContext(), "Выберите свою активность", Toast.LENGTH_SHORT).show();
                    return;
                }

                RelativeLayout relativeActivity = v.findViewById(R.id.activityRel);
                relativeActivity.setVisibility(View.GONE);
                RelativeLayout relativeData = v.findViewById(R.id.dataRel);
                relativeData.setVisibility(View.VISIBLE);
            }
        });
        //  Страница заполнения данных
        EditText age_text = v.findViewById(R.id.age);
        EditText growth_text = v.findViewById(R.id.growth);
        EditText weight_text = v.findViewById(R.id.weight);
        Button calculate = v.findViewById(R.id.but_data_nextClick);

        calculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (age_text.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Введите свой возраст", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (growth_text.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Введите свой рост", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (weight_text.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Введите свой вес", Toast.LENGTH_SHORT).show();
                    return;
                }

                RelativeLayout relativeData = v.findViewById(R.id.dataRel);
                relativeData.setVisibility(View.GONE);
                RelativeLayout relativeResult = v.findViewById(R.id.resultRel);
                relativeResult.setVisibility(View.VISIBLE);
                TextView result_text = v.findViewById(R.id.result);

                double activity = Double.parseDouble(text_activity_noVisible.getText().toString());
                double weight = Double.parseDouble(weight_text.getText().toString());
                int growth = Integer.parseInt(growth_text.getText().toString());
                int age = Integer.parseInt(age_text.getText().toString());
                double result;

                if (text_gender_noVisible.getText().toString().equals("Мужской")) {
                    result = (10 * weight + 6.25 * growth - 5 * age + 5) * activity;
                    result_text.setText(result + " калорий");
                }
                if (text_gender_noVisible.getText().toString().equals("Женский")) {
                    result = (10 * weight + 6.25 * growth - 5 * age - 161) * activity;
                    result_text.setText(result + " калорий");
                }
            }
        });
        return v;
    }
}

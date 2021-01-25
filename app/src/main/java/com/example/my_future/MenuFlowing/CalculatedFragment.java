package com.example.my_future.MenuFlowing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
            }
        });

        return v;
    }
}

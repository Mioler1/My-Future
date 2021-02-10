package com.example.my_future.Additionally;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_ACTIVITY;

public class FragmentActivityActivism extends AppCompatActivity {
    TextView text_activity_noVisible;
    ImageButton but_activity_1;
    ImageButton but_activity_2;
    ImageButton but_activity_3;
    ImageButton but_activity_4;
    ImageButton but_activity_5;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_activisim);

        init();
        click();
    }

    private void init() {
        mSettings = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        Button next_activity = findViewById(R.id.but_activity_nextClick);
        text_activity_noVisible = findViewById(R.id.visible_text_activity);
        progressBar = findViewById(R.id.progressBarActive);
        but_activity_1 = findViewById(R.id.image_click_activity_1);
        but_activity_2 = findViewById(R.id.image_click_activity_2);
        but_activity_3 = findViewById(R.id.image_click_activity_3);
        but_activity_4 = findViewById(R.id.image_click_activity_4);
        but_activity_5 = findViewById(R.id.image_click_activity_5);

        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activity_text = text_activity_noVisible.getText().toString();
                if (activity_text.isEmpty()) {
                    MyToast("Выберите свою активность");
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myRef.child(mAuth.getUid()).child("health").child("activity").setValue(activity_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_ACTIVITY, activity_text);
                        editor.apply();
                        MyToast("Готово");
                        onBackPressed();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не изменил");
                        onBackPressed();
                        finish();
                    }
                });
            }
        });
    }

    private void click() {
        but_activity_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.2");
                but_activity_1.setBackgroundResource(R.drawable.border_for_calc_active);
                but_activity_2.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_3.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_4.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_5.setBackgroundResource(R.drawable.border_for_calc);
            }
        });
        but_activity_2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.375");
                but_activity_2.setBackgroundResource(R.drawable.border_for_calc_active);
                but_activity_1.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_3.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_4.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_5.setBackgroundResource(R.drawable.border_for_calc);
            }
        });
        but_activity_3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.55");
                but_activity_3.setBackgroundResource(R.drawable.border_for_calc_active);
                but_activity_2.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_1.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_4.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_5.setBackgroundResource(R.drawable.border_for_calc);
            }
        });
        but_activity_4.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.725");
                but_activity_4.setBackgroundResource(R.drawable.border_for_calc_active);
                but_activity_2.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_3.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_1.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_5.setBackgroundResource(R.drawable.border_for_calc);
            }
        });
        but_activity_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_activity_noVisible.setText("1.9");
                but_activity_5.setBackgroundResource(R.drawable.border_for_calc_active);
                but_activity_2.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_3.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_4.setBackgroundResource(R.drawable.border_for_calc);
                but_activity_1.setBackgroundResource(R.drawable.border_for_calc);
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

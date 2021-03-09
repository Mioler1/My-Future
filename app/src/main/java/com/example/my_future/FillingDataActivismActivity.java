package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_ACTIVITY;
import static com.example.my_future.Variables.CHECK_DATA_ACTIVISM;

public class FillingDataActivismActivity extends AppCompatActivity {
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    SharedPreferences mSettings, checkDataSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data_activism);

        init();
        onClickSaveDataActivism();
    }

    private void init() {
        mSettings = this.getSharedPreferences(ALL_DATA_USER, MODE_PRIVATE);
        checkDataSettings = this.getSharedPreferences(ALL_CHECK_DATA, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSaveDataActivism() {
        TextView textNoVisibleActivity = findViewById(R.id.visible_text_activity);
        ImageButton but_activity_1 = findViewById(R.id.image_click_activity_1);
        ImageButton but_activity_2 = findViewById(R.id.image_click_activity_2);
        ImageButton but_activity_3 = findViewById(R.id.image_click_activity_3);
        ImageButton but_activity_4 = findViewById(R.id.image_click_activity_4);
        ImageButton but_activity_5 = findViewById(R.id.image_click_activity_5);
        Button next_activity = findViewById(R.id.but_activity_nextClick);

        but_activity_1.setOnClickListener(view -> textNoVisibleActivity.setText("1.2"));
        but_activity_2.setOnClickListener(view -> textNoVisibleActivity.setText("1.375"));
        but_activity_3.setOnClickListener(view -> textNoVisibleActivity.setText("1.55"));
        but_activity_4.setOnClickListener(view -> textNoVisibleActivity.setText("1.725"));
        but_activity_5.setOnClickListener(view -> textNoVisibleActivity.setText("1.9"));

        next_activity.setOnClickListener(view -> {
            String text_activity = textNoVisibleActivity.getText().toString();
            if (text_activity.isEmpty()) {
                MyToast("Выберите свою активность");
                return;
            }
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_DATA_USER_ACTIVITY, text_activity);
            editor.apply();

            SharedPreferences.Editor editorCheck = checkDataSettings.edit();
            editorCheck.putString(CHECK_DATA_ACTIVISM, "true");
            editorCheck.apply();

            myRef.child(mAuth.getUid()).child("health").child("activity").setValue(text_activity);
            finish();
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataActivismActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
package com.example.my_future.Additionally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_future.Adapters.PressureAdapter;
import com.example.my_future.Models.Pressure;
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_PRESSURE;

public class FragmentActivityPressure extends AppCompatActivity {
    TextView textNoVisiblePressure;
    RecyclerView recyclerPressure;
    ProgressBar progressBar;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pressure);

        init();
        pressureSelection();
    }

    private void init() {
        mSettings = this.getSharedPreferences(ALL_DATA_USER, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        recyclerPressure = findViewById(R.id.pressureList);
        progressBar = findViewById(R.id.progressBar);
    }

    public void onClickSaveDataPressure(View view) {
        String text_pressure = textNoVisiblePressure.getText().toString();
        if (text_pressure.isEmpty()) {
            MyToast("Выберите давление");
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRef.child(mAuth.getUid()).child("health").child("pressure").setValue(text_pressure);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_DATA_USER_PRESSURE, text_pressure);
                editor.apply();
                onBackPressed();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Не сменил");
                onBackPressed();
                finish();
            }
        });
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

        PressureAdapter.OnUserClickListener onUserClickListener = new PressureAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(Pressure pressure) {
                textNoVisiblePressure.setText(pressure.getNamePressure());
            }
        };

        PressureAdapter pressureAdapter = new PressureAdapter(this, pressureArrayList, onUserClickListener);
        recyclerPressure.setAdapter(pressureAdapter);
    }

    private void MyToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
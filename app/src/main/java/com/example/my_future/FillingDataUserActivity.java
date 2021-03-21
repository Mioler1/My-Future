package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_AVATAR;
import static com.example.my_future.Variables.APP_DATA_USER_GENDER;
import static com.example.my_future.Variables.APP_DATA_USER_GROWTH;
import static com.example.my_future.Variables.APP_DATA_USER_NICKNAME;
import static com.example.my_future.Variables.APP_DATA_USER_TARGET;
import static com.example.my_future.Variables.APP_DATA_USER_WEIGHT;

public class FillingDataUserActivity extends AppCompatActivity {
    EditText nickname, weight, growth;
    RadioGroup gender;
    Spinner target;
    TextView textNoVisibleGender, textNoVisibleTarget;
    ProgressBar progressBarDataUser;
    CircleImageView avatar_img;
    Button buttonSave;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    StorageReference mStorageRef;

    SharedPreferences mSettings;
    Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data_user);

        init();
        genderSelection();
        targetSelection();
    }

    private void init() {
        mSettings = this.getSharedPreferences(ALL_DATA_USER, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");

        // Иннициализация в окне данные пользователя
        nickname = findViewById(R.id.nickname);
        weight = findViewById(R.id.weight);
        growth = findViewById(R.id.growth);
        gender = findViewById(R.id.gender);
        target = findViewById(R.id.target);
        progressBarDataUser = findViewById(R.id.progressBarUser);
        avatar_img = findViewById(R.id.avatar);
        buttonSave = findViewById(R.id.but_save);

        avatar_img.setOnClickListener(v -> {
            Intent uploadIntent = new Intent();
            uploadIntent.setType("image/*");
            uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(uploadIntent, 1);
        });
    }

    public void onClickSaveDataUser(View view) {
        String nickname_text = nickname.getText().toString();
        String weight_text = weight.getText().toString();
        String growth_text = growth.getText().toString();
        boolean check;

        if (nickname_text.isEmpty()) {
            MyToast("Поле никнейм пустое");
            return;
        }
        if (nickname_text.length() < 3) {
            MyToast("Никнейм короткий");
        }
        if (nickname_text.matches("[a-zA-Zа-яА-Я0-9_-]+")) {
            check = true;
        } else {
            MyToast("Некоректный никнейм");
            return;
        }
        if (!check) {
            MyToast("Некоректный никнейм");
            return;
        }

        if (weight_text.isEmpty()) {
            MyToast("Поле вес пустое");
            return;
        }
        double weight_num = Double.parseDouble(weight_text);
        if (weight_num > 300) {
            MyToast("Наврятли ты такой толстый");
            return;
        }
        if (weight_num < 30) {
            MyToast("Наврятли ты такой дрыщ");
            return;
        }

        if (growth_text.isEmpty()) {
            MyToast("Поле рост пустое");
            return;
        }
        double growth_num = Double.parseDouble(growth_text);
        if (growth_num > 400) {
            MyToast("Наврятли ты такой высокий");
            return;
        }
        if (growth_num < 50) {
            MyToast("Наврятли ты такой карлик");
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
        if (!nickname_text.isEmpty() && weight_text.isEmpty() && !growth_text.isEmpty() && !textNoVisibleGender.getText().toString().isEmpty() && !textNoVisibleTarget.getText().toString().isEmpty()) {
            progressBarDataUser.setVisibility(View.VISIBLE);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                user.setNickname(nickname_text);
                user.setWeight(weight_text);
                user.setGrowth(growth.getText().toString());
                user.setGender(textNoVisibleGender.getText().toString());
                user.setTarget(textNoVisibleTarget.getText().toString());
                if (uploadUri != null) {
                    user.setAvatar(uploadUri.toString());
                } else {
                    user.setAvatar("default");
                }

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_DATA_USER_NICKNAME, nickname_text);
                editor.putString(APP_DATA_USER_WEIGHT, weight_text);
                editor.putString(APP_DATA_USER_GROWTH, growth_text);
                editor.putString(APP_DATA_USER_GENDER, textNoVisibleGender.getText().toString());
                editor.putString(APP_DATA_USER_TARGET, textNoVisibleTarget.getText().toString());
                editor.putString(APP_DATA_USER_AVATAR, String.valueOf(uploadUri));
                editor.apply();

                myRef.child(mAuth.getUid()).child("profile").setValue(user);
                startActivity(new Intent(FillingDataUserActivity.this, IntroActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void genderSelection() {
        gender.clearCheck();
        textNoVisibleGender = findViewById(R.id.visible_text_gender);
        gender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.gender_men:
                    textNoVisibleGender.setText("Мужской");
                    break;
                case R.id.gender_girl:
                    textNoVisibleGender.setText("Женский");
                    break;
            }
        });
    }

    private void MyToast(String message) {
        Toast.makeText(FillingDataUserActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                avatar_img.setImageURI(data.getData());
                progressBarDataUser.setVisibility(View.VISIBLE);
                buttonSave.setEnabled(false);
                buttonSave.setBackgroundResource(R.drawable.btn_save_disabled);
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        String date = dateFormat.format(new Date());
        Bitmap bitmap = ((BitmapDrawable) avatar_img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        MyToast(String.valueOf(byteArray.length));
        if (byteArray.length <= 5242880) {
            final StorageReference myStorage = mStorageRef.child(System.currentTimeMillis() + " " + date);
            UploadTask uploadTask = myStorage.putBytes(byteArray);
            uploadTask.continueWithTask(task ->
                    myStorage.getDownloadUrl()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            uploadUri = task.getResult();
                            buttonSave.setEnabled(true);
                            buttonSave.setBackgroundResource(R.drawable.btn_save_actived);
                            progressBarDataUser.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(e -> MyToast("Картинка не загрузилась"));
        } else {
            MyToast("Размер картинки не более 5мб");
            avatar_img.setImageResource(R.drawable.default_avatar);
            buttonSave.setEnabled(true);
            buttonSave.setBackgroundResource(R.drawable.btn_save_actived);
            progressBarDataUser.setVisibility(View.GONE);
        }
    }

    private void targetSelection() {
        textNoVisibleTarget = findViewById(R.id.visible_text_target);
        String[] targets = getResources().getStringArray(R.array.target);

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
}
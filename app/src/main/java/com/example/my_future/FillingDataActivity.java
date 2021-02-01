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
import android.util.Base64;
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

import com.example.my_future.Intro.IntroActivity;
import com.example.my_future.MenuBottom.SharedPrefferences.SaveDataUser;
import com.example.my_future.Models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class FillingDataActivity extends AppCompatActivity {

    EditText nickname, weight;
    RadioGroup gender;
    Spinner target;
    TextView textNoVisibleGender;
    TextView textNoVisibleTarget;
    ProgressBar progressBar;
    CircleImageView avatar_img;

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    StorageReference mStorageRef;
    SharedPreferences mSettings;
    Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_data);

        init();
        genderSelection();
        targetSelection();
    }

    private void init() {
        FillingDataActivity fillingDataActivityClass = FillingDataActivity.this;
        mSettings = fillingDataActivityClass.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        nickname = findViewById(R.id.nickname);
        weight = findViewById(R.id.weight);
        gender = findViewById(R.id.gender);
        target = findViewById(R.id.target);
        progressBar = findViewById(R.id.progressBar);
        avatar_img = findViewById(R.id.avatar);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");

        avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent();
                uploadIntent.setType("image/*");
                uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(uploadIntent, 1);
            }
        });
    }

    public void onClickSaveFillingData(View view) {
        String nickname_text = nickname.getText().toString();
        boolean check;
        String weight_text = weight.getText().toString();
        double weight_num = Double.parseDouble(weight_text);

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
        if (weight.getText().toString().isEmpty()) {
            MyToast("Поле вес пустое");
            return;
        }
        if (weight_num > 300) {
            MyToast("Наврятли ты такой толстый");
            return;
        }
        if (weight_num < 30) {
            MyToast("Наврятли ты такой дрыщ");
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
                user.setNickname(nickname_text);
                user.setWeight(weight.getText().toString());
                user.setGender(textNoVisibleGender.getText().toString());
                user.setTarget(textNoVisibleTarget.getText().toString());
                if (uploadUri != null) {
                    user.setAvatar(uploadUri.toString());
                } else {
                    user.setAvatar("default");
                }

                Bitmap bitmap = ((BitmapDrawable) avatar_img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_NICKNAME, nickname_text);
                editor.putString(APP_PREFERENCES_WEIGHT, weight_text);
                editor.putString(APP_PREFERENCES_TARGET, textNoVisibleTarget.getText().toString());
                editor.putString(APP_PREFERENCES_AVATAR, encodedImage);
                editor.apply();

                myRef.child(mAuth.getUid()).child("profile").setValue(user);
                startActivity(new Intent(FillingDataActivity.this, IntroActivity.class));
                finish();
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

    private void MyToast(String message) {
        Toast.makeText(FillingDataActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                avatar_img.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) avatar_img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference myStorage = mStorageRef.child(System.currentTimeMillis() + "my_avatar");
        UploadTask uploadTask = myStorage.putBytes(byteArray);
        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return myStorage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MyToast("Картинка не загрузилась");
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
}
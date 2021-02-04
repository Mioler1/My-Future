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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_BICEPS;
import static com.example.my_future.Variables.APP_PREFERENCES_CHEST;
import static com.example.my_future.Variables.APP_PREFERENCES_FOREARM;
import static com.example.my_future.Variables.APP_PREFERENCES_GROWTH;
import static com.example.my_future.Variables.APP_PREFERENCES_HIP;
import static com.example.my_future.Variables.APP_PREFERENCES_NECK;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_SHIN;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.APP_PREFERENCES_WAIST;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class FillingDataUserActivity extends AppCompatActivity {

    // Окно с данными пользователя
    EditText nickname, weight, growth;
    RadioGroup gender;
    Spinner target;
    TextView textNoVisibleGender, textNoVisibleTarget;
    ProgressBar progressBarDataUser;
    ProgressBar progressBarDataVolume;
    CircleImageView avatar_img;

    // Окно с объёмами тела
    EditText waist, neck, chest, biceps, forearm, hip, shin;

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
        FillingDataUserActivity fillingDataUserActivityClass = FillingDataUserActivity.this;
        mSettings = fillingDataUserActivityClass.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
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

        // Иннициализация в окне объёмы пользователя
        waist = findViewById(R.id.waist);
        neck = findViewById(R.id.neck);
        chest = findViewById(R.id.chest);
        biceps = findViewById(R.id.biceps);
        forearm = findViewById(R.id.forearm);
        hip = findViewById(R.id.hip);
        shin = findViewById(R.id.shin);
        progressBarDataVolume = findViewById(R.id.progressBarVolume);

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

        if (weight.getText().toString().isEmpty()) {
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
        if (growth_num > 300) {
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

        if (!nickname_text.isEmpty() && !weight.getText().toString().isEmpty() && !growth_text.isEmpty() && !textNoVisibleGender.getText().toString().isEmpty() && !textNoVisibleTarget.getText().toString().isEmpty()) {
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

                Bitmap bitmap = ((BitmapDrawable) avatar_img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_NICKNAME, nickname_text);
                editor.putString(APP_PREFERENCES_WEIGHT, weight_text);
                editor.putString(APP_PREFERENCES_GROWTH, growth.getText().toString());
                editor.putString(APP_PREFERENCES_TARGET, textNoVisibleTarget.getText().toString());
                editor.putString(APP_PREFERENCES_AVATAR, encodedImage);
                editor.apply();

                myRef.child(mAuth.getUid()).child("profile").setValue(user);

                RelativeLayout relativeDataUser = findViewById(R.id.AutoLayout);
                RelativeLayout relativeDataVolume = findViewById(R.id.AutoLayout2);
                relativeDataUser.setVisibility(View.GONE);
                avatar_img.setVisibility(View.GONE);
                relativeDataVolume.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Данные не добавились");
            }
        });
    }

    public void onClickSaveDataVolume(View view) {
        String waist_text = waist.getText().toString();
        String neck_text = neck.getText().toString();
        String chest_text = chest.getText().toString();
        String biceps_text = biceps.getText().toString();
        String forearm_text = forearm.getText().toString();
        String hip_text = hip.getText().toString();
        String shin_text = shin.getText().toString();

        SharedPreferences.Editor editor = mSettings.edit();
        if (!waist_text.isEmpty() && !neck_text.isEmpty() && !chest_text.isEmpty() && !biceps_text.isEmpty() && !forearm_text.isEmpty() && !hip_text.isEmpty() && !shin_text.isEmpty()) {
            progressBarDataVolume.setVisibility(View.VISIBLE);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!waist_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue(waist_text);
                    editor.putString(APP_PREFERENCES_WAIST, waist_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("waist").setValue("—");
                    editor.putString(APP_PREFERENCES_WAIST, "—");
                }

                if (!neck_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue(neck_text);
                    editor.putString(APP_PREFERENCES_NECK, neck_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("neck").setValue("—");
                    editor.putString(APP_PREFERENCES_NECK, "—");
                }

                if (!chest_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue(chest_text);
                    editor.putString(APP_PREFERENCES_CHEST, chest_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("chest").setValue("—");
                    editor.putString(APP_PREFERENCES_CHEST, "—");
                }

                if (!biceps_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue(biceps_text);
                    editor.putString(APP_PREFERENCES_BICEPS, biceps_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("biceps").setValue("—");
                    editor.putString(APP_PREFERENCES_BICEPS, "—");
                }

                if (!forearm_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue(forearm_text);
                    editor.putString(APP_PREFERENCES_FOREARM, forearm_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("forearm").setValue("—");
                    editor.putString(APP_PREFERENCES_FOREARM, "—");
                }

                if (!hip_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue(hip_text);
                    editor.putString(APP_PREFERENCES_HIP, hip_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("hip").setValue("—");
                    editor.putString(APP_PREFERENCES_HIP, "—");
                }

                if (!shin_text.isEmpty()) {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue(shin_text);
                    editor.putString(APP_PREFERENCES_SHIN, shin_text);
                } else {
                    myRef.child(mAuth.getUid()).child("volume").child("shin").setValue("—");
                    editor.putString(APP_PREFERENCES_SHIN, "—");
                }

                editor.apply();
                startActivity(new Intent(FillingDataUserActivity.this, FillingDataUserHealthActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
        Toast.makeText(FillingDataUserActivity.this, message, Toast.LENGTH_SHORT).show();
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
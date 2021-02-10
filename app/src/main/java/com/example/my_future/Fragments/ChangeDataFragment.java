package com.example.my_future.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.my_future.Additionally.FragmentActivityPressure;
import com.example.my_future.AuthorizationActivity;
import com.example.my_future.Additionally.FragmentActivityActivism;
import com.example.my_future.MenuBottom.ProfileFragment;
import com.example.my_future.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
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

public class ChangeDataFragment extends Fragment implements BackPressed {
    TextView textNoVisibleTargetChange, textNoVisibleDiseases, textNoVisibleExperience;
    CircleImageView avatar_img_change;
    Spinner target_change, diseases_change, experience_change;
    View v, viewAlert;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    AlertDialog alertDialog;
    SharedPreferences mSettings;
    Uri uploadUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_change_data, container, false);
        init();
        clickOpenAlert();
        return v;
    }

    private void init() {
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");
        mUser = mAuth.getCurrentUser();


        ImageView back = v.findViewById(R.id.OnClickBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });
    }

    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();
        viewAlert = inflater.inflate(R.layout.alert_dialog_change_data, null);
        avatar_img_change = viewAlert.findViewById(R.id.avatar_change);
        builder.setView(viewAlert).setCancelable(true);
        alertDialog = builder.create();
        progressBar = viewAlert.findViewById(R.id.progressBar);
        avatar_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent();
                uploadIntent.setType("image/*");
                uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(uploadIntent, 1);
            }
        });
        alertDialog.show();
    }

    //  Методы смены данных пользователя
    public void openChangeEmail() {
        openAlertDialog();
        EditText email_change = viewAlert.findViewById(R.id.email_change);
        EditText old_password = viewAlert.findViewById(R.id.old_password);
        email_change.setVisibility(View.VISIBLE);
        old_password.setVisibility(View.VISIBLE);

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_change.getText().toString().isEmpty()) {
                    MyToast("Введите email");
                    return;
                }
                if (old_password.getText().toString().isEmpty()) {
                    MyToast("Введите старый пароль");
                    return;
                }
                if (mAuth.getUid().equals(email_change.getText().toString())) {
                    MyToast("Введен текущий email");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String passwordOld = snapshot.child(mAuth.getUid()).child("password").getValue().toString();
                        if (!old_password.getText().toString().equals(passwordOld)) {
                            MyToast("Неправильный старый пароль");
                            return;
                        }
                        mUser.updateEmail(email_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            alertDialog.dismiss();
                                            MyToast("Зайдите на почту");
                                            myRef.child(mAuth.getUid()).child("email").setValue(email_change.getText().toString());
                                            mAuth.signOut();
                                            startActivity(new Intent(getContext(), AuthorizationActivity.class));
                                        }
                                    });
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                        MyToast("Неверный адрес");
                                    } catch (FirebaseAuthUserCollisionException existEmail) {
                                        MyToast("Данный email уже используется");
                                    } catch (Exception e) {
                                        MyToast(e.getMessage());
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangePassword() {
        openAlertDialog();
        EditText old_password = viewAlert.findViewById(R.id.old_password);
        EditText password_change = viewAlert.findViewById(R.id.password_change);
        EditText repeat_password_change = viewAlert.findViewById(R.id.repeat_password_change);
        old_password.setVisibility(View.VISIBLE);
        password_change.setVisibility(View.VISIBLE);
        repeat_password_change.setVisibility(View.VISIBLE);

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_password.getText().toString().isEmpty()) {
                    MyToast("Введите старый пароль");
                    return;
                }
                if (password_change.getText().toString().isEmpty()) {
                    MyToast("Введите пароль");
                    return;
                }
                if (password_change.getText().toString().length() < 6) {
                    MyToast("Пароль не меньше 6 символов");
                    return;
                }
                if (repeat_password_change.getText().toString().isEmpty()) {
                    MyToast("Введите повторный пароль");
                    return;
                }
                if (!password_change.getText().toString().equals(repeat_password_change.getText().toString())) {
                    MyToast("Повторный пароль введен не верно");
                    return;
                }

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String passwordOld = snapshot.child(mAuth.getUid()).child("password").getValue().toString();
                        if (!old_password.getText().toString().equals(passwordOld)) {
                            MyToast("Неправильный старый пароль");
                            return;
                        }
                        if (password_change.getText().toString().equals(passwordOld)) {
                            MyToast("Вы вводите старый пароль");
                            return;
                        }
                        mUser.updatePassword(password_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    myRef.child(mAuth.getUid()).child("password").setValue(password_change.getText().toString());
                                    MyToast("Готово");
                                    alertDialog.dismiss();
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (Exception e) {
                                        MyToast(e.getMessage());
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeNickname() {
        openAlertDialog();
        EditText nickname_change = viewAlert.findViewById(R.id.nickname_change);

        if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
            nickname_change.setText(mSettings.getString(APP_PREFERENCES_NICKNAME, ""));
        }

        nickname_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname_text = nickname_change.getText().toString();
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
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("profile").child("nickname").setValue(nickname_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_NICKNAME, nickname_text);
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    public void openChangeWeight() {
        openAlertDialog();
        EditText weight_change = viewAlert.findViewById(R.id.weight_change);

        weight_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight_text = weight_change.getText().toString();
                double weight_num = Double.parseDouble(weight_text);
                if (weight_text.isEmpty()) {
                    MyToast("Введите новый вес");
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
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("profile").child("weight").setValue(weight_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_WEIGHT, weight_text);
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeGrowth() {
        openAlertDialog();
        EditText growth_change = viewAlert.findViewById(R.id.growth_change);

        growth_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String growth_text = growth_change.getText().toString();
                int growth_num = Integer.parseInt(growth_text);
                if (growth_text.isEmpty()) {
                    MyToast("Введите новый вес");
                }
                if (growth_num > 300) {
                    MyToast("Наврятли ты такой высокий");
                    return;
                }
                if (growth_num < 50) {
                    MyToast("Наврятли ты такой карлик");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("profile").child("growth").setValue(growth_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_GROWTH, growth_text);
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeTarget() {
        openAlertDialog();
        target_change = viewAlert.findViewById(R.id.target_change);
        target_change.setVisibility(View.VISIBLE);
        targetSelection();
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textNoVisibleTargetChange.getText().toString().equals("Выберите цель")) {
                    MyToast("Выберите цель");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("profile").child("target").setValue(textNoVisibleTargetChange.getText().toString());
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_TARGET, textNoVisibleTargetChange.getText().toString());
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeAvatar() {
        openAlertDialog();
        avatar_img_change.setVisibility(View.VISIBLE);

        if (mSettings.contains(APP_PREFERENCES_AVATAR)) {
            String mImageUri = mSettings.getString("Avatar", "");
            byte[] decode = Base64.decode(mImageUri, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            avatar_img_change.setImageBitmap(bitmap);
        }

        Button butSave = viewAlert.findViewById(R.id.butSaveChangeDate);
        butSave.setBackgroundResource(R.drawable.btn_save_disabled);
        butSave.setEnabled(false);

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("MyLog", String.valueOf(uploadUri));
                        myRef.child(mAuth.getUid()).child("profile").child("avatar").setValue(String.valueOf(uploadUri));

                        Bitmap bitmap = ((BitmapDrawable) avatar_img_change.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] byteArray = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_AVATAR, encodedImage);
                        editor.apply();

                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    //  Методы смены данных здоровья пользователя
    public void openChangeDiseases() {
        openAlertDialog();
        diseases_change = viewAlert.findViewById(R.id.diseases_change);
        diseases_change.setVisibility(View.VISIBLE);
        diseasesSelection();

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diseases_text = textNoVisibleDiseases.getText().toString();
                if (diseases_text.equals("Выберите заболевание")) {
                    MyToast("Выберите заболевание");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("health").child("experience").setValue(diseases_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_TARGET, diseases_text);
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeActivity() {
        startActivity(new Intent(getContext(), FragmentActivityActivism.class));
    }

    public void openChangePressure() {
        startActivity(new Intent(getContext(), FragmentActivityPressure.class));
    }

    public void openChangeExperience() {
        openAlertDialog();
        experience_change = viewAlert.findViewById(R.id.experience_change);
        experience_change.setVisibility(View.VISIBLE);
        experienceSelection();

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String experience_text = textNoVisibleExperience.getText().toString();
                if (experience_text.equals("Выберите стаж")) {
                    MyToast("Выберите стаж");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        myRef.child(mAuth.getUid()).child("health").child("experience").setValue(experience_text);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_TARGET, experience_text);
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void openChangeVolume() {
        openAlertDialog();
        EditText waist = viewAlert.findViewById(R.id.waist);
        EditText neck = viewAlert.findViewById(R.id.neck);
        EditText chest = viewAlert.findViewById(R.id.chest);
        EditText biceps = viewAlert.findViewById(R.id.biceps);
        EditText forearm = viewAlert.findViewById(R.id.forearm);
        EditText hip = viewAlert.findViewById(R.id.hip);
        EditText shin = viewAlert.findViewById(R.id.shin);

        waist.setVisibility(View.VISIBLE);
        neck.setVisibility(View.VISIBLE);
        chest.setVisibility(View.VISIBLE);
        biceps.setVisibility(View.VISIBLE);
        forearm.setVisibility(View.VISIBLE);
        hip.setVisibility(View.VISIBLE);
        shin.setVisibility(View.VISIBLE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mSettings.contains(APP_PREFERENCES_WAIST)) {
                    waist.setText(mSettings.getString(APP_PREFERENCES_WAIST, ""));
                } else {
                    waist.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("waist").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_NECK)) {
                    neck.setText(mSettings.getString(APP_PREFERENCES_NECK, ""));
                } else {
                    neck.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("neck").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_CHEST)) {
                    chest.setText(mSettings.getString(APP_PREFERENCES_CHEST, ""));
                } else {
                    chest.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("chest").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_BICEPS)) {
                    biceps.setText(mSettings.getString(APP_PREFERENCES_BICEPS, ""));
                } else {
                    biceps.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("biceps").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_FOREARM)) {
                    forearm.setText(mSettings.getString(APP_PREFERENCES_FOREARM, ""));
                } else {
                    forearm.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("forearm").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_HIP)) {
                    hip.setText(mSettings.getString(APP_PREFERENCES_HIP, ""));
                } else {
                    hip.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("hip").getValue()));
                }
                if (mSettings.contains(APP_PREFERENCES_SHIN)) {
                    shin.setText(mSettings.getString(APP_PREFERENCES_SHIN, ""));
                } else {
                    shin.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("volume").child("shin").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyToast("Что то не загрузилось");
            }
        });

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waist_text = waist.getText().toString();
                String neck_text = neck.getText().toString();
                String chest_text = chest.getText().toString();
                String biceps_text = biceps.getText().toString();
                String forearm_text = forearm.getText().toString();
                String hip_text = hip.getText().toString();
                String shin_text = shin.getText().toString();

                SharedPreferences.Editor editor = mSettings.edit();
                if (!waist_text.isEmpty() && !neck_text.isEmpty() && !chest_text.isEmpty() && !biceps_text.isEmpty() && !forearm_text.isEmpty() && !hip_text.isEmpty() && !shin_text.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
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
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Данные не добавились");
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    //  Методы...
    private void clickOpenAlert() {
        TextView clickEmail = v.findViewById(R.id.openChangeEmail);
        TextView clickPassword = v.findViewById(R.id.openChangePassword);
        TextView clickNickname = v.findViewById(R.id.openChangeNickname);
        TextView clickAvatar = v.findViewById(R.id.openChangeAvatar);
        TextView clickWight = v.findViewById(R.id.openChangeWeight);
        TextView clickGrowth = v.findViewById(R.id.openChangeGrowth);
        TextView clickTarget = v.findViewById(R.id.openChangeTarget);

        TextView clickDiseases = v.findViewById(R.id.openChangeDiseases);
        TextView clickActivity = v.findViewById(R.id.openChangeActivity);
        TextView clickPressure = v.findViewById(R.id.openChangePressure);
        TextView clickExperience = v.findViewById(R.id.openChangeExperience);
        TextView clickVolume = v.findViewById(R.id.openChangeVolume);

        clickEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeEmail();
            }
        });
        clickPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangePassword();
            }
        });
        clickNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeNickname();
            }
        });
        clickAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeAvatar();
            }
        });
        clickWight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeWeight();
            }
        });
        clickGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeGrowth();
            }
        });
        clickTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeTarget();
            }
        });
        clickDiseases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeDiseases();
            }
        });
        clickActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeActivity();
            }
        });
        clickPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangePressure();
            }
        });
        clickExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeExperience();
            }
        });
        clickVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeVolume();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
//                avatar_img_change.setVisibility(View.GONE);
                avatar_img_change.setImageURI(data.getData());
//                progressBar.setVisibility(View.VISIBLE);
//                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(avatar_img_change.getLayoutParams());
//                marginParams.setMargins(0, 0, 0, 20);
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
//                avatar_img_change.setLayoutParams(layoutParams);
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) avatar_img_change.getDrawable()).getBitmap();
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
                if (task.isSuccessful()) {
                    uploadUri = task.getResult();
                    viewAlert.findViewById(R.id.butSaveChangeDate).setEnabled(true);
                    viewAlert.findViewById(R.id.butSaveChangeDate).setBackgroundResource(R.drawable.btn_save_actived);
//                    avatar_img_change.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MyToast("Картинка не загрузилась");
            }
        });
    }

    private void targetSelection() {
        textNoVisibleTargetChange = viewAlert.findViewById(R.id.visible_text_target_change);
        String[] targets = getResources().getStringArray(R.array.target);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, targets) {
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
        target_change.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleTargetChange.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        target_change.setOnItemSelectedListener(itemSelectedListener);
    }

    private void diseasesSelection() {
        textNoVisibleDiseases = viewAlert.findViewById(R.id.visible_text_diseases);
        String[] diseasesS = getResources().getStringArray(R.array.diseases);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, diseasesS) {
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
        diseases_change.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleDiseases.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        diseases_change.setOnItemSelectedListener(itemSelectedListener);
    }

    private void experienceSelection() {
        textNoVisibleExperience = viewAlert.findViewById(R.id.visible_text_experience);
        String[] experiences = getResources().getStringArray(R.array.experience);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, experiences) {
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
        experience_change.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textNoVisibleExperience.setText((CharSequence) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        experience_change.setOnItemSelectedListener(itemSelectedListener);
    }

    private void MyToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        return true;
    }
}

package com.example.my_future.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.my_future.AuthorizationActivity;
import com.example.my_future.FirstScreenActivity;
import com.example.my_future.MenuBottom.ProfileFragment;
import com.example.my_future.R;
import com.example.my_future.StartScreenActivity;
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
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.my_future.Variables.APP_PREFERENCES;
import static com.example.my_future.Variables.APP_PREFERENCES_AVATAR;
import static com.example.my_future.Variables.APP_PREFERENCES_NICKNAME;
import static com.example.my_future.Variables.APP_PREFERENCES_TARGET;
import static com.example.my_future.Variables.APP_PREFERENCES_WEIGHT;

public class ChangeDataFragment extends Fragment implements BackPressed {
    TextView textNoVisibleTargetChange;
    CircleImageView avatar_img_change;
    Spinner target_change;
    View v, viewAlert;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    AlertDialog alertDialog;
    Uri uploadUri;

    EditText nickname_change;
    SharedPreferences mSettings;

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

    public void openChangeEmail() {
        openAlertDialog();
        EditText email_change = viewAlert.findViewById(R.id.email_change);
        email_change.setVisibility(View.VISIBLE);

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_change.getText().toString().isEmpty()) {
                    MyToast("Введите email");
                    return;
                }
                if (mAuth.getUid().equals(email_change.getText().toString())) {
                    MyToast("Введен текущий email");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser.updateEmail(email_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
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
                    }
                });
            }
        });
    }

    public void openChangeNickname() {
        openAlertDialog();
        nickname_change = viewAlert.findViewById(R.id.nickname_change);
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
                if (weight_change.getText().toString().isEmpty()) {
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
                        myRef.child(mAuth.getUid()).child("profile").child("weight").setValue(weight_change.getText().toString());
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_WEIGHT, weight_change.getText().toString());
                        editor.apply();
                        MyToast("Готово");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        MyToast("Не сменил");
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
                if (textNoVisibleTargetChange.getText().toString().equals("Выбрать новую цель")) {
                    MyToast("Выберите новую цель");
                    return;
                }
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    }
                });
            }
        });
    }

    public void openChangeAvatar() {
        openAlertDialog();
        avatar_img_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (String.valueOf(uploadUri).equals("null")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("MyLog", String.valueOf(uploadUri));
                                    myRef.child(mAuth.getUid()).child("profile").child("avatar").setValue(String.valueOf(uploadUri));
                                }
                            }, 6000);
                        } else {
                            Log.d("MyLog", String.valueOf(uploadUri));
                            myRef.child(mAuth.getUid()).child("profile").child("avatar").setValue(String.valueOf(uploadUri));
                        }

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
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                avatar_img_change.setImageURI(data.getData());
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
        textNoVisibleTargetChange = viewAlert.findViewById(R.id.visible_text_target_change);
        String[] targets = {"Выбрать новую цель", "Похудеть", "Рельеф", "Мышечная масса", "Сила"};

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

    private void MyToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void clickOpenAlert() {
        TextView clickEmail = v.findViewById(R.id.openChangeEmail);
        TextView clickPassword = v.findViewById(R.id.openChangePassword);
        TextView clickNickname = v.findViewById(R.id.openChangeNickname);
        TextView clickAvatar = v.findViewById(R.id.openChangeAvatar);
        TextView clickWight = v.findViewById(R.id.openChangeWeight);
        TextView clickTarget = v.findViewById(R.id.openChangeTarget);

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
        clickTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeTarget();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        return true;
    }
}

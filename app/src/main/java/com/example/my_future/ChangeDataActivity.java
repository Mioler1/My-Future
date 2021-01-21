package com.example.my_future;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangeDataActivity extends AppCompatActivity {

    TextView textNoVisibleTargetChange;
    ImageView avatar_img_change;
    ProgressBar progressBar;
    Spinner target_change;
    View viewAlert;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    AlertDialog alertDialog;
    Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);

        init();
    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");
        mUser = mAuth.getCurrentUser();
    }

    private void MyToast(String message) {
        Toast.makeText(ChangeDataActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        openAlertDialog();
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

    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeDataActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        LayoutInflater inflater = getLayoutInflater();
        viewAlert = inflater.inflate(R.layout.alert_dialog_change_data, null);
        avatar_img_change = viewAlert.findViewById(R.id.avatar_change);
        builder.setView(viewAlert).setCancelable(false);
        alertDialog = builder.create();

        viewAlert.findViewById(R.id.butCloseAlertDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

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

    public void openChangeEmail(View view) {
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
                                            startActivity(new Intent(ChangeDataActivity.this, AuthorizationActivity.class));
                                            finish();
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

    public void openChangePassword(View view) {
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
                            MyToast("Старый пароль введен");
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

    public void openChangeNickname(View view) {
        openAlertDialog();
        EditText nickname_change = viewAlert.findViewById(R.id.nickname_change);
        nickname_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myRef.child(mAuth.getUid()).child("profile").child("nickname").setValue(nickname_change.getText().toString());
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

    public void openChangeWeight(View view) {
        openAlertDialog();
        EditText weight_change = viewAlert.findViewById(R.id.weight_change);
        weight_change.setVisibility(View.VISIBLE);
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myRef.child(mAuth.getUid()).child("profile").child("weight").setValue(weight_change.getText().toString());
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

    public void openChangeTarget(View view) {
        openAlertDialog();
        target_change = viewAlert.findViewById(R.id.target_change);
        target_change.setVisibility(View.VISIBLE);
        targetSelection();
        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myRef.child(mAuth.getUid()).child("profile").child("target").setValue(textNoVisibleTargetChange.getText().toString());
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

    public void openChangeAvatar(View view) {
        openAlertDialog();
        avatar_img_change.setVisibility(View.VISIBLE);

        viewAlert.findViewById(R.id.butSaveChangeDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("MyLog", String.valueOf(uploadUri));
                        myRef.child(mAuth.getUid()).child("profile").child("avatar").setValue(String.valueOf(uploadUri));
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
}
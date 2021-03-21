package com.example.my_future.MenuBottom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.my_future.Fragments.ChangeDataFragment;
import com.example.my_future.R;
import com.example.my_future.TabLayout.DepthPageTransformer;
import com.example.my_future.TabLayout.Profile.GraphFragment;
import com.example.my_future.TabLayout.Profile.HealthFragment;
import com.example.my_future.TabLayout.Profile.PageAdapter;
import com.example.my_future.TabLayout.Profile.SportResultFragment;
import com.example.my_future.TabLayout.Profile.VolumesBodyFragment;
import com.google.android.material.tabs.TabLayout;
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

import static android.app.Activity.RESULT_OK;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.APP_DATA_USER_AVATAR;
import static com.example.my_future.Variables.APP_DATA_USER_GENDER;
import static com.example.my_future.Variables.APP_DATA_USER_GROWTH;
import static com.example.my_future.Variables.APP_DATA_USER_NICKNAME;
import static com.example.my_future.Variables.APP_DATA_USER_TARGET;
import static com.example.my_future.Variables.APP_DATA_USER_WEIGHT;
import static com.example.my_future.Variables.TAG;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    SharedPreferences mSettings;

    CircleImageView avatar, changeAvatar;
    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;

    View viewFragment, viewAlert;
    Button buttonSave;
    ProgressBar progressBarDataUser;
    AlertDialog alertDialog;

    String urlAvatar = "";
    Uri uploadUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.bottom_fragment_profile, container, false);
        init();
        return viewFragment;
    }

    private void init() {
        avatar = viewFragment.findViewById(R.id.avatar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");
        mSettings = viewFragment.getContext().getSharedPreferences(ALL_DATA_USER, Context.MODE_PRIVATE);

        viewPager = viewFragment.findViewById(R.id.viewPager);
        tabLayout = viewFragment.findViewById(R.id.tabLayout);

        pageAdapter = new PageAdapter(getFragmentManager());
        pageAdapter.addFragment(new VolumesBodyFragment());
        pageAdapter.addFragment(new SportResultFragment());
        pageAdapter.addFragment(new HealthFragment());
        pageAdapter.addFragment(new GraphFragment());

        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setSaveFromParentEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        int[] iconResId = {R.drawable.ic_person,
                R.drawable.ic_fitness_black,
                R.drawable.ic_health,
                R.drawable.ic_graph};
        for (int i = 0; i < iconResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(iconResId[i]);
        }

        downloadData();
        changeAvatar();
        viewFragment.findViewById(R.id.ic_changeData).setOnClickListener(view ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeDataFragment()).commit());
    }

    private void downloadData() {
        TextView nickname = viewFragment.findViewById(R.id.nickname);
        TextView target = viewFragment.findViewById(R.id.target);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences.Editor editor = mSettings.edit();
                if (mSettings.contains(APP_DATA_USER_AVATAR)) {
                    String user_avatar = mSettings.getString(APP_DATA_USER_AVATAR, "");
                    Glide.with(avatar.getContext()).load(user_avatar).error(R.drawable.default_avatar).into(avatar);
                } else {
                    urlAvatar = String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue());
                    Glide.with(avatar.getContext()).load(urlAvatar).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            editor.putString(APP_DATA_USER_AVATAR, urlAvatar);
                            editor.apply();
                            return false;
                        }
                    }).error(R.drawable.default_avatar).into(avatar);
                }
                if (mSettings.contains(APP_DATA_USER_NICKNAME)) {
                    nickname.setText(mSettings.getString(APP_DATA_USER_NICKNAME, ""));
                } else {
                    nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
                }
                if (mSettings.contains(APP_DATA_USER_TARGET)) {
                    target.setText(mSettings.getString(APP_DATA_USER_TARGET, ""));
                } else {
                    target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
                }
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeAvatar() {
        avatar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialog);
            LayoutInflater inflater = getLayoutInflater();
            viewAlert = inflater.inflate(R.layout.alert_dialog_change_avatar, null);
            changeAvatar = viewAlert.findViewById(R.id.avatar_change);
            buttonSave = viewAlert.findViewById(R.id.butSave);
            progressBarDataUser = viewAlert.findViewById(R.id.progressBar);
            viewAlert.findViewById(R.id.change_avatar).setOnClickListener(vChange -> {
                Intent uploadIntent = new Intent();
                uploadIntent.setType("image/*");
                uploadIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(uploadIntent, 1);
            });
            viewAlert.findViewById(R.id.delete_avatar).setOnClickListener(vDelete -> {
                deleteAvatar();
                alertDialog.dismiss();
            });
            builder.setView(viewAlert).setCancelable(true);
            alertDialog = builder.create();
            alertDialog.show();

            buttonSave.setOnClickListener(vSave -> {
                deleteAvatar();
                myRef.child(mAuth.getUid()).child("profile").child("avatar").setValue(uploadUri.toString());
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_DATA_USER_AVATAR, String.valueOf(uploadUri));
                editor.apply();
                alertDialog.dismiss();
            });
        });
    }

    private void deleteAvatar() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference;
        if (mSettings.contains(APP_DATA_USER_AVATAR)) {
            storageReference = firebaseStorage.getReferenceFromUrl(mSettings.getString(APP_DATA_USER_AVATAR, ""));
        } else {
            storageReference = firebaseStorage.getReferenceFromUrl(urlAvatar);
        }
        storageReference.delete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                RelativeLayout relativeSelection = viewAlert.findViewById(R.id.RelSelectionClick);
                RelativeLayout relativeChange = viewAlert.findViewById(R.id.RelChangeAvatar);
                relativeSelection.setVisibility(View.GONE);
                relativeChange.setVisibility(View.VISIBLE);
                progressBarDataUser.setVisibility(View.VISIBLE);
                buttonSave.setEnabled(false);
                buttonSave.setBackgroundResource(R.drawable.btn_save_disabled);
                changeAvatar.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        String date = dateFormat.format(new Date());
        Bitmap bitmap = ((BitmapDrawable) changeAvatar.getDrawable()).getBitmap();
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
            changeAvatar.setImageResource(R.drawable.default_avatar);
            buttonSave.setEnabled(true);
            buttonSave.setBackgroundResource(R.drawable.btn_save_actived);
            progressBarDataUser.setVisibility(View.GONE);
        }
    }

    private void MyToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

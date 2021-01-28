package com.example.my_future.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.my_future.ChangeDataActivity;
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeDataFragment extends Fragment {
    TextView textNoVisibleTargetChange;
    CircleImageView avatar_img_change;
    ProgressBar progressBar;
    Spinner target_change;
    View v, viewAlert;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    FirebaseUser mUser;

    AlertDialog alertDialog;
    Uri uploadUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_change_data, container, false);
        init();
        return v;
    }

    private void init() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Avatars");
        mUser = mAuth.getCurrentUser();
    }
    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
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
}

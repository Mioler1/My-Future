package com.example.my_future.MenuBottom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.my_future.ChangeDataActivity;
import com.example.my_future.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_activity_profile, container, false);
        Button button = (Button) v.findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeDataActivity.class));
            }
        });
        CircleImageView avatar = (CircleImageView) v.findViewById(R.id.avatar);
        TextView nickname = (TextView) v.findViewById(R.id.nickname);
        TextView target = (TextView) v.findViewById(R.id.target);
        TextView weight = (TextView) v.findViewById(R.id.weight);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(avatar).load(snapshot.child(mAuth.getUid()).child("profile").child("avatar").getValue()).error(R.drawable.standart_avatar).into(avatar);
                nickname.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("nickname").getValue()));
                target.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("target").getValue()));
                weight.setText(String.valueOf(snapshot.child(mAuth.getUid()).child("profile").child("weight").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}

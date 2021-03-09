package com.example.my_future.MenuFlowing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.CalculetedActivity.CalculatedKetchActivity;
import com.example.my_future.CalculetedActivity.CalculetedMifflinActivity;
import com.example.my_future.R;

public class CalculatedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.left_fragment_calculated, container, false);
        TextView calcMif = v.findViewById(R.id.calcMif);
        TextView calcKetch = v.findViewById(R.id.calcKetch);

        calcMif.setOnClickListener(view -> startActivity(new Intent(getContext(), CalculetedMifflinActivity.class)));
        calcKetch.setOnClickListener(view -> startActivity(new Intent(getContext(), CalculatedKetchActivity.class)));
        return v;
    }
}

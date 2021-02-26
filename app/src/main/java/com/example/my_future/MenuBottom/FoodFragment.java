package com.example.my_future.MenuBottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.MenuFlowing.CalculatedFragment;
import com.example.my_future.R;

public class FoodFragment extends Fragment {
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.bottom_fragment_food, container, false);
        init();
        return v;
    }

    private void init() {

        Button button = v.findViewById(R.id.calc);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalculatedFragment()).commit();
            }
        });
    }

}


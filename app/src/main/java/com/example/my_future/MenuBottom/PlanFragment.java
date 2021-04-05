package com.example.my_future.MenuBottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.my_future.R;

public class PlanFragment extends Fragment {
    View viewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Тренировки");
        viewFragment = inflater.inflate(R.layout.bottom_fragment_training, container, false);

        init();
        return viewFragment;
    }

    private void init() {
        ImageButton but_activ1 = viewFragment.findViewById(R.id.Button_AllTrains);

        ImageButton but_activ2 = viewFragment.findViewById(R.id.Button_MyTrains);
        but_activ1.setOnClickListener(view -> {
            but_activ1.setBackgroundResource(R.drawable.button_full_solid);
            but_activ2.setBackgroundResource(R.drawable.button_border_blue);
        });
        but_activ2.setOnClickListener(view -> {
            but_activ2.setBackgroundResource(R.drawable.button_full_solid2);
            but_activ1.setBackgroundResource(R.drawable.button_border_blue2);
        });
    }
}

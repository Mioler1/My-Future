package com.example.my_future.TabLayout.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.R;

import static com.example.my_future.Variables.ALL_SAVE_SETTINGS;
import static com.example.my_future.Variables.SAVE_CHECKBOX_AEROBICS;
import static com.example.my_future.Variables.SAVE_CHECKBOX_ALL;
import static com.example.my_future.Variables.SAVE_CHECKBOX_ATHLETICS;
import static com.example.my_future.Variables.SAVE_CHECKBOX_BODYBUILDING;
import static com.example.my_future.Variables.SAVE_CHECKBOX_GYMNASTICS;

public class SportResultFragment extends Fragment {
    View v, viewAlert;
    RelativeLayout RelBody, RelAtl, RelGym, RelAer;
    SharedPreferences saveSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_sport_result, container, false);
        init();
        checkCheckbox();
        return v;
    }

    private void init() {
        saveSettings = getContext().getSharedPreferences(ALL_SAVE_SETTINGS, Context.MODE_PRIVATE);
        v.findViewById(R.id.filter).setOnClickListener(view -> openAlertDialogFilter());
        RelBody = v.findViewById(R.id.RelSportBody);
        RelAtl = v.findViewById(R.id.RelSportAth);
        RelGym = v.findViewById(R.id.RelSportGym);
        RelAer = v.findViewById(R.id.RelSportAer);
    }

    private void checkCheckbox() {
        if (!saveSettings.getString(SAVE_CHECKBOX_BODYBUILDING, "").equals("true")) {
            RelBody.setVisibility(View.GONE);
        } else {
            RelBody.setVisibility(View.VISIBLE);
        }
        if (!saveSettings.getString(SAVE_CHECKBOX_ATHLETICS, "").equals("true")) {
            RelAtl.setVisibility(View.GONE);
        } else {
            RelAtl.setVisibility(View.VISIBLE);
        }
        if (!saveSettings.getString(SAVE_CHECKBOX_GYMNASTICS, "").equals("true")) {
            RelGym.setVisibility(View.GONE);
        } else {
            RelGym.setVisibility(View.VISIBLE);
        }
        if (!saveSettings.getString(SAVE_CHECKBOX_AEROBICS, "").equals("true")) {
            RelAer.setVisibility(View.GONE);
        } else {
            RelAer.setVisibility(View.VISIBLE);
        }
    }

    private void openAlertDialogFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_MyAlertDialog);
        viewAlert = getLayoutInflater().inflate(R.layout.alert_dialog_filter_sport_result, null);
        builder.setView(viewAlert).setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        viewAlert.findViewById(R.id.but_save).setOnClickListener(view -> {
            checkCheckbox();
            alertDialog.dismiss();
        });
        SharedPreferences.Editor editor = saveSettings.edit();
        CheckBox checkBoxAll = viewAlert.findViewById(R.id.checkBoxAll);
        CheckBox checkBoxBody = viewAlert.findViewById(R.id.checkBoxBody);
        CheckBox checkBoxAth = viewAlert.findViewById(R.id.checkBoxAth);
        CheckBox checkBoxGym = viewAlert.findViewById(R.id.checkBoxGym);
        CheckBox checkBoxAer = viewAlert.findViewById(R.id.checkBoxAer);

        if (saveSettings.getString(SAVE_CHECKBOX_ALL, "").equals("true")) {
            checkBoxAll.setChecked(true);
            checkBoxBody.setChecked(true);
            checkBoxAth.setChecked(true);
            checkBoxGym.setChecked(true);
            checkBoxAer.setChecked(true);
        } else {
            checkBoxAll.setChecked(false);
            checkBoxBody.setChecked(false);
            checkBoxAth.setChecked(false);
            checkBoxGym.setChecked(false);
            checkBoxAer.setChecked(false);
        }
        if (saveSettings.getString(SAVE_CHECKBOX_BODYBUILDING, "").equals("true")) {
            checkBoxBody.setChecked(true);
        } else {
            checkBoxBody.setChecked(false);
        }
        if (saveSettings.getString(SAVE_CHECKBOX_ATHLETICS, "").equals("true")) {
            checkBoxAth.setChecked(true);
        } else {
            checkBoxAth.setChecked(false);
        }
        if (saveSettings.getString(SAVE_CHECKBOX_GYMNASTICS, "").equals("true")) {
            checkBoxGym.setChecked(true);
        } else {
            checkBoxGym.setChecked(false);
        }
        if (saveSettings.getString(SAVE_CHECKBOX_AEROBICS, "").equals("true")) {
            checkBoxAer.setChecked(true);
        } else {
            checkBoxAer.setChecked(false);
        }

        checkBoxAll.setOnClickListener(view -> {
            if (checkBoxAll.isChecked()) {
                editor.putString(SAVE_CHECKBOX_ALL, "true");
                editor.putString(SAVE_CHECKBOX_BODYBUILDING, "true");
                editor.putString(SAVE_CHECKBOX_ATHLETICS, "true");
                editor.putString(SAVE_CHECKBOX_GYMNASTICS, "true");
                editor.putString(SAVE_CHECKBOX_AEROBICS, "true");
                checkBoxBody.setChecked(true);
                checkBoxAth.setChecked(true);
                checkBoxGym.setChecked(true);
                checkBoxAer.setChecked(true);
            } else {
                editor.putString(SAVE_CHECKBOX_ALL, "false");
                editor.putString(SAVE_CHECKBOX_BODYBUILDING, "false");
                editor.putString(SAVE_CHECKBOX_ATHLETICS, "false");
                editor.putString(SAVE_CHECKBOX_GYMNASTICS, "false");
                editor.putString(SAVE_CHECKBOX_AEROBICS, "false");
                checkBoxBody.setChecked(false);
                checkBoxAth.setChecked(false);
                checkBoxGym.setChecked(false);
                checkBoxAer.setChecked(false);
            }
            editor.apply();
        });
        checkBoxBody.setOnClickListener(view -> {
            if (checkBoxBody.isChecked()) {
                editor.putString(SAVE_CHECKBOX_BODYBUILDING, "true");
                if (checkBoxAth.isChecked() && checkBoxGym.isChecked() && checkBoxAer.isChecked()) {
                    editor.putString(SAVE_CHECKBOX_ALL, "true");
                    checkBoxAll.setChecked(true);
                }
            } else {
                editor.putString(SAVE_CHECKBOX_BODYBUILDING, "false");
                editor.putString(SAVE_CHECKBOX_ALL, "false");
                checkBoxAll.setChecked(false);
            }
            editor.apply();
        });
        checkBoxAth.setOnClickListener(view -> {
            if (checkBoxAth.isChecked()) {
                editor.putString(SAVE_CHECKBOX_ATHLETICS, "true");
                if (checkBoxBody.isChecked() && checkBoxGym.isChecked() && checkBoxAer.isChecked()) {
                    editor.putString(SAVE_CHECKBOX_ALL, "true");
                    checkBoxAll.setChecked(true);
                }
            } else {
                editor.putString(SAVE_CHECKBOX_ATHLETICS, "false");
                editor.putString(SAVE_CHECKBOX_ALL, "false");
                checkBoxAll.setChecked(false);
            }
            editor.apply();
        });
        checkBoxGym.setOnClickListener(view -> {
            if (checkBoxGym.isChecked()) {
                editor.putString(SAVE_CHECKBOX_GYMNASTICS, "true");
                if (checkBoxBody.isChecked() && checkBoxAth.isChecked() && checkBoxAer.isChecked()) {
                    editor.putString(SAVE_CHECKBOX_ALL, "true");
                    checkBoxAll.setChecked(true);
                }
            } else {
                editor.putString(SAVE_CHECKBOX_GYMNASTICS, "false");
                editor.putString(SAVE_CHECKBOX_ALL, "false");
                checkBoxAll.setChecked(false);
            }
            editor.apply();
        });
        checkBoxAer.setOnClickListener(view -> {
            if (checkBoxAer.isChecked()) {
                editor.putString(SAVE_CHECKBOX_AEROBICS, "true");
                if (checkBoxBody.isChecked() && checkBoxAth.isChecked() && checkBoxGym.isChecked()) {
                    editor.putString(SAVE_CHECKBOX_ALL, "true");
                    checkBoxAll.setChecked(true);
                }
            } else {
                editor.putString(SAVE_CHECKBOX_AEROBICS, "false");
                editor.putString(SAVE_CHECKBOX_ALL, "false");
                checkBoxAll.setChecked(false);
            }
            editor.apply();
        });
    }
}

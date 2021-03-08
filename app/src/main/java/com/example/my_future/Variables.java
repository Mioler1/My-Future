package com.example.my_future;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Variables {
    public static final String TAG = "MyLog";
    public static final String APP_PREFERENCES = "saveDataUser";
    public static final List<Fragment> fragmentsInStack = new ArrayList<>();
    public static final List<Fragment> fragmentsInStackFlowing = new ArrayList<>();

    //  Переменные данных пользователя
    public static final String APP_PREFERENCES_NICKNAME = "Nickname";
    public static final String APP_PREFERENCES_WEIGHT = "Weight";
    public static final String APP_PREFERENCES_GROWTH = "Growth";
    public static final String APP_PREFERENCES_GENDER = "Gender";
    public static final String APP_PREFERENCES_TARGET = "Target";
    public static final String APP_PREFERENCES_AVATAR = "Avatar";

    //  Переменные объёма пользователя
    public static final String APP_PREFERENCES_WAIST = "Waist";
    public static final String APP_PREFERENCES_NECK = "Neck";
    public static final String APP_PREFERENCES_CHEST = "Chest";
    public static final String APP_PREFERENCES_BICEPS = "Biceps";
    public static final String APP_PREFERENCES_FOREARM = "Forearm";
    public static final String APP_PREFERENCES_HIP = "Hip";
    public static final String APP_PREFERENCES_SHIN = "Shin";

    //  Переменные данных здоровья пользователя
    public static final String APP_PREFERENCES_PRESSURE = "Pressure";
    public static final String APP_PREFERENCES_DISEASES = "Diseases";
    public static final String APP_PREFERENCES_EXPERIENCE = "Experience";
    public static final String APP_PREFERENCES_ACTIVITY = "Activity";

    //  Переменные проверки заполнения данных
    public static final String APP_PREFERENCES_BOOLEAN_PROFILE = "ProfileBool";
    public static final String APP_PREFERENCES_BOOLEAN_VOLUME = "VolumeBool";
    public static final String APP_PREFERENCES_BOOLEAN_SPORT = "SportBool";
    public static final String APP_PREFERENCES_BOOLEAN_HEALTH = "HealthBool";
    public static final String APP_PREFERENCES_BOOLEAN_ACTIVITY = "ActivityBool";
}

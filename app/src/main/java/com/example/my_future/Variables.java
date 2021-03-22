package com.example.my_future;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Variables {
    public static final String TAG = "TAG";
    public static final List<Fragment> fragmentsInStack = new ArrayList<>();
    public static final List<Fragment> fragmentsInStackFlowing = new ArrayList<>();

    //  Переменные сохранения
    public static final String ALL_DATA_USER = "saveDataUser";
    public static final String ALL_DATA_AVATAR = "saveDataAvatar";
    public static final String ALL_CHECK_DATA = "saveCheckData";

    //  Переменные данных пользователя
    public static final String APP_DATA_USER_NICKNAME = "Nickname";
    public static final String APP_DATA_USER_WEIGHT = "Weight";
    public static final String APP_DATA_USER_GROWTH = "Growth";
    public static final String APP_DATA_USER_GENDER = "Gender";
    public static final String APP_DATA_USER_TARGET = "Target";
    public static final String APP_DATA_AVATAR = "Avatar";

    //  Переменные объёма пользователя
    public static final String APP_DATA_USER_WAIST = "Waist";
    public static final String APP_DATA_USER_NECK = "Neck";
    public static final String APP_DATA_USER_CHEST = "Chest";
    public static final String APP_DATA_USER_BICEPS = "Biceps";
    public static final String APP_DATA_USER_FOREARM = "Forearm";
    public static final String APP_DATA_USER_HIP = "Hip";
    public static final String APP_DATA_USER_SHIN = "Shin";

    //  Переменные данных здоровья пользователя
    public static final String APP_DATA_USER_PRESSURE = "Pressure";
    public static final String APP_DATA_USER_DISEASES = "Diseases";
    public static final String APP_DATA_USER_EXPERIENCE = "Experience";
    public static final String APP_DATA_USER_ACTIVITY = "Activity";

    //  Переменные проверки заполнения данных
    public static final String CHECK_DATA_PROFILE = "ProfileBool";
    public static final String CHECK_DATA_VOLUME = "VolumeBool";
    public static final String CHECK_DATA_SPORT = "SportBool";
    public static final String CHECK_DATA_HEALTH = "HealthBool";
    public static final String CHECK_DATA_ACTIVISM = "ActivismBool";
}

package com.example.my_future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Intro extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPager introViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //setup ViewPager

        List<ScreenItemLayout> mList = new ArrayList<>();
        mList.add(new ScreenItemLayout("List1","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.chat));
        mList.add(new ScreenItemLayout("List2","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.eat));
        mList.add(new ScreenItemLayout("List3","Loremasffsafffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",R.drawable.profile));

        screenPager = findViewById(R.id.Pager1);
        introViewPager = new IntroViewPager(this,mList);
        screenPager.setAdapter(introViewPager);
    }
}
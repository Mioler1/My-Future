package com.example.my_future.TabLayout.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> mFragmentList;

    public PageAdapter(Fragment fa) {
        super(fa);
        mFragmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}

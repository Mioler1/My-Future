package com.example.my_future.TabLayout.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentsInStackTab = new ArrayList<>();

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        fragmentsInStackTab.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsInStackTab.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsInStackTab.size();
    }
}

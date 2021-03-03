package com.example.my_future.TabLayout.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.example.my_future.Variables.fragmentsInStackTab;
import static com.example.my_future.Variables.stringList;

public class PageAdapter extends FragmentStatePagerAdapter {

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentsInStackTab.add(fragment);
        stringList.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsInStackTab.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsInStackTab.size();
    }
}

package com.example.my_future.Menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.my_future.R;
import com.google.android.material.navigation.NavigationView;

public class MenuListFragment extends Fragment {
    NavigationView vNavigation;
    private NavItemSelectedListener navItemSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        vNavigation = view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                navItemSelectedListener.onNavItemSelectedListener(menuItem);
                return false;
            }
        });
//        fontFor();
        return view;
    }

    public void setNavItemSelectedListener(NavItemSelectedListener navItemSelectedListener) {
        this.navItemSelectedListener = navItemSelectedListener;
    }

//    private void fontFor() {
//        Menu menu = vNavigation.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem menuItem = menu.getItem(i);
//
//            SubMenu subMenu = menuItem.getSubMenu();
//            if (subMenu != null && subMenu.size() > 0) {
//                for (int j = 0; j < subMenu.size(); j++) {
//                    MenuItem subMenuItem = subMenu.getItem(j);
//                    applyFontToMenuItem(subMenuItem);
//                }
//            }
//            applyFontToMenuItem(menuItem);
//        }
//    }
//
//    private void applyFontToMenuItem(MenuItem menuItem) {
//        if (getActivity() != null) {
//            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font));
//            SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
//            mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            menuItem.setTitle(mNewTitle);
//        }
//    }


}
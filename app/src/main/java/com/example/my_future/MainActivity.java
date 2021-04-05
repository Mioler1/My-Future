package com.example.my_future;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.my_future.MenuBottom.FoodFragment;
import com.example.my_future.MenuBottom.ForumFragment;
import com.example.my_future.MenuBottom.NotebookFragment;
import com.example.my_future.MenuBottom.PlanFragment;
import com.example.my_future.MenuBottom.ProfileFragment;
import com.example.my_future.MenuLeft.CalculatedFragment;
import com.example.my_future.MenuLeft.DrawerAdapter;
import com.example.my_future.MenuLeft.DrawerItem;
import com.example.my_future.MenuLeft.SimpleItem;
import com.example.my_future.MenuLeft.SpaceItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import static com.example.my_future.Variables.ALL_CHECK_DATA;
import static com.example.my_future.Variables.ALL_DATA_AVATAR;
import static com.example.my_future.Variables.ALL_DATA_USER;
import static com.example.my_future.Variables.CHECK_DATA_PROFILE;
import static com.example.my_future.Variables.fragmentsInStack;
import static com.example.my_future.Variables.fragmentsInStackFlowing;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    FirebaseDatabase db;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    Toolbar toolbar;
    BottomNavigationView bottomNav;
    SharedPreferences mSettings, checkDataSettings, avatarSettings;

    private final FoodFragment fragmentFood = new FoodFragment();
    private final ForumFragment fragmentForum = new ForumFragment();
    private final NotebookFragment fragmentNotebook = new NotebookFragment();
    private final PlanFragment fragmentPlan = new PlanFragment();
    private final ProfileFragment fragmentProfile = new ProfileFragment();

    private static final int POS_PROGRESS = 0;
    private static final int POS_LICENSE = 1;
    private static final int POS_HELP = 2;
    private static final int POS_CALCULATE = 3;
    private static final int POS_SETTINGS = 4;
    private static final int POS_LOGOUT = 6;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        openMenu(savedInstanceState);
        clickBottomNavigationMenu();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottom_navigation);
        fragmentsInStack.add(fragmentPlan);
        changeFragment(fragmentPlan);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        mSettings = getSharedPreferences(ALL_DATA_USER, MODE_PRIVATE);
        checkDataSettings = getSharedPreferences(ALL_CHECK_DATA, MODE_PRIVATE);
        avatarSettings = getSharedPreferences(ALL_DATA_AVATAR, MODE_PRIVATE);
        checkProfile();
    }

    private void openMenu(Bundle saved) {
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(saved)
                .withMenuLayout(R.layout.menu_left)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_PROGRESS).setChecked(true),
                createItemFor(POS_LICENSE),
                createItemFor(POS_HELP),
                createItemFor(POS_CALCULATE),
                createItemFor(POS_SETTINGS),
                new SpaceItem(50),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(int position) {
        Fragment selectedFragment = null;
        if (position == POS_PROGRESS) {
//            selectedFragment = ;
        }
        if (position == POS_LICENSE) {
//            selectedFragment = ;
        }
        if (position == POS_HELP) {
//            selectedFragment = ;
        }
        if (position == POS_CALCULATE) {
            selectedFragment = new CalculatedFragment();
            toolbar.setTitle("Калькуляторы");
        }
        if (position == POS_SETTINGS) {
//            selectedFragment = ;
        }
        if (position == POS_LOGOUT) {
            mSettings.edit().clear().apply();
            avatarSettings.edit().clear().apply();
            fragmentsInStack.clear();
            fragmentsInStackFlowing.clear();
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, FirstScreenActivity.class));
            finish();
        }
        slidingRootNav.closeMenu();
        if (selectedFragment != null) {
            if (!fragmentsInStackFlowing.isEmpty()) {
                fragmentsInStackFlowing.clear();
            }
            fragmentsInStackFlowing.remove(selectedFragment);
            fragmentsInStackFlowing.add(selectedFragment);
            changeFragment(selectedFragment);

        }
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private void clickBottomNavigationMenu() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment targetFragment = itemId == R.id.fragmentPlan ? fragmentPlan
                    : itemId == R.id.fragmentFood ? fragmentFood
                    : itemId == R.id.fragmentForum ? fragmentForum
                    : itemId == R.id.fragmentNotebook ? fragmentNotebook
                    : fragmentProfile;
            fragmentsInStack.remove(targetFragment);
            fragmentsInStack.add(targetFragment);

            if (!fragmentsInStackFlowing.isEmpty()) {
                fragmentsInStackFlowing.clear();
            }
            changeFragment(targetFragment);

            return true;
        });
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!fragmentsInStackFlowing.isEmpty()) {
            fragmentsInStackFlowing.remove(fragmentsInStackFlowing.size() - 1);
            int lastId = fragmentsInStack.size() - 1;
            changeFragment(fragmentsInStack.get(lastId));
        } else {
            if (!fragmentsInStack.isEmpty()) {
                fragmentsInStack.remove(fragmentsInStack.size() - 1);
                int lastId = fragmentsInStack.size() - 1;
                if (lastId != -1) {
                    changeFragment(fragmentsInStack.get(lastId));
                    bottomNav.setSelectedItemId(
                            fragmentsInStack.get(lastId) == fragmentPlan ? R.id.fragmentPlan
                                    : fragmentsInStack.get(lastId) == fragmentFood ? R.id.fragmentFood
                                    : fragmentsInStack.get(lastId) == fragmentForum ? R.id.fragmentForum
                                    : fragmentsInStack.get(lastId) == fragmentNotebook ? R.id.fragmentNotebook
                                    : R.id.fragmentProfile
                    );
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    private void MyToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkProfile() {
        SharedPreferences.Editor editor = checkDataSettings.edit();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!String.valueOf(checkDataSettings.contains(CHECK_DATA_PROFILE)).equals("true")) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(mAuth.getUid())) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                if (s.getKey().equals("profile")) {
                                    if (s.getValue().equals("none")) {
                                        startActivity(new Intent(MainActivity.this, FillingDataUserActivity.class));
                                        fragmentsInStack.clear();
                                        finish();
                                    } else {
                                        editor.putString(CHECK_DATA_PROFILE, "true");
                                        editor.apply();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyToast(databaseError.getMessage());
            }
        });
    }
}
package com.example.my_future.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.my_future.R;

import java.util.List;

public class IntroViewPager extends PagerAdapter {


    Context mContext;
    List<IntroItem> mListScreen;

    public IntroViewPager(Context mContext, List<IntroItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreenIntro = inflater.inflate(R.layout.layout_screen_intro, null);

        ImageView ScreenImg = layoutScreenIntro.findViewById(R.id.imageIntro);
        TextView title = layoutScreenIntro.findViewById(R.id.intro_title);
        TextView description = layoutScreenIntro.findViewById(R.id.intro_description);

        title.setText(mListScreen.get(position).getTitle());
        description.setText(mListScreen.get(position).getDescription());
        ScreenImg.setImageResource(mListScreen.get(position).getScreenImg());

        container.addView(layoutScreenIntro);

        return layoutScreenIntro;

    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
}

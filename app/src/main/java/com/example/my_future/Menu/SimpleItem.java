package com.example.my_future.Menu;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_future.R;

public class SimpleItem extends com.example.my_future.Menu.DrawerItem<SimpleItem.ViewHolder> {
    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private Drawable icon;
    private String title;

    public SimpleItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
        holder.icon.setImageDrawable(icon);

        holder.title.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);
        holder.icon.setColorFilter(isChecked ? selectedItemIconTint : normalItemIconTint);
    }

    public com.example.my_future.Menu.SimpleItem withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }

    public com.example.my_future.Menu.SimpleItem withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public com.example.my_future.Menu.SimpleItem withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public com.example.my_future.Menu.SimpleItem withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends com.example.my_future.Menu.DrawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}

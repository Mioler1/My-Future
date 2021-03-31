package com.example.my_future.Menu;

import android.view.ViewGroup;

public abstract class DrawerItem<T extends com.example.my_future.Menu.DrawerAdapter.ViewHolder> {

    protected boolean isChecked;

    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    public com.example.my_future.Menu.DrawerItem<T> setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isSelectable() {
        return true;
    }
}

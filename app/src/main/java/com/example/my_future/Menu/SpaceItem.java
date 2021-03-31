package com.example.my_future.Menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class SpaceItem extends com.example.my_future.Menu.DrawerItem<SpaceItem.ViewHolder> {

    private int spaceDp;

    public SpaceItem(int spaceDp) {
        this.spaceDp = spaceDp;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        Context c = parent.getContext();
        View view = new View(c);
        int height = (int) (c.getResources().getDisplayMetrics().density*spaceDp);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        ));
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {

    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    public class ViewHolder extends com.example.my_future.Menu.DrawerAdapter.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

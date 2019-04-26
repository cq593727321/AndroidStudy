package com.smartcomma.huawei.Entity;

import android.graphics.drawable.Drawable;

public class HomeItem {
    private int mDrawable;
    private String menuName;

    public HomeItem(int mDrawable, String menuName) {
        this.mDrawable = mDrawable;
        this.menuName = menuName;
    }

    public int getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(int mDrawable) {
        this.mDrawable = mDrawable;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}

package com.kevinlinxc.guitarautoscroll.CustomClasses;

import android.app.Application;

import java.util.List;

public class ASApplication extends Application {
    private static List<Tab> mTabs;

    public static List<Tab> getmTabs() {
        return mTabs;
    }

    public static void setmTabs(List<Tab> tabs) {
        mTabs = tabs;
    }
}
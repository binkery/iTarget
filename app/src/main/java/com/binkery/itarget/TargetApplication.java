package com.binkery.itarget;

import android.app.Application;

import com.binkery.itarget.sqlite.DBHelper;

/**
 */

public class TargetApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance().init(this);

    }

}

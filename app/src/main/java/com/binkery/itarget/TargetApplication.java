package com.binkery.itarget;

import android.app.Application;

import com.binkery.itarget.sqlite.DBHelper;

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
public class TargetApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance().init(this);

    }

}

package com.binkery.itarget.sqlite;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 */

public class DBHelper {

    private AppDatabase mAppDatabase;

    private DBHelper() {

    }

    private static class Inner {
        private static final DBHelper instance = new DBHelper();
    }

    public static DBHelper getInstance() {
        return Inner.instance;
    }

    public void init(Context context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "target.db")
                .allowMainThreadQueries()
                .build();
    }

    public TargetDao targetDao() {
        return mAppDatabase.targetDao();
    }

    public ItemDao itemDao() {
        return mAppDatabase.itemDao();
    }

}

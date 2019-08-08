package com.binkery.itarget.sqlite;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
@Database(entities = {TargetEntity.class, ItemEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract TargetDao targetDao();

    public abstract ItemDao itemDao();
}


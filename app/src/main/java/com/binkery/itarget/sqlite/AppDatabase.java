package com.binkery.itarget.sqlite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 */
@Database(entities = {TargetEntity.class, ItemEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract TargetDao targetDao();

    public abstract ItemDao itemDao();
}


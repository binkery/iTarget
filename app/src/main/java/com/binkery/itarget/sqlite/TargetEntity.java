package com.binkery.itarget.sqlite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 */
@Entity(tableName = "targets")
public class TargetEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "uuid")
    public String uuid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "type")
    public int type;

}

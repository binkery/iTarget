package com.binkery.itarget.sqlite;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
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

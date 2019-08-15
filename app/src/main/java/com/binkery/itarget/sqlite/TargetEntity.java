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

    @ColumnInfo(name = "data_1")
    public String data1;

    @ColumnInfo(name = "data_2")
    public String data2;

    @ColumnInfo(name = "data_3")
    public String data3;

    @ColumnInfo(name = "data_4")
    public String data4;

    @ColumnInfo(name = "data_5")
    public String data5;

    @ColumnInfo(name = "data_6")
    public String data6;

    @ColumnInfo(name = "data_7")
    public String data7;

    @ColumnInfo(name = "data_8")
    public String data8;

}

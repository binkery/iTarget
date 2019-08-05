package com.binkery.itarget.sqlite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 */
@Entity(tableName = "items")
public class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "uuid")
    public String uuid;

    @ColumnInfo(name = "target_id")
    public int targetId;

    @ColumnInfo(name = "start_time")
    public long startTime;

    @ColumnInfo(name = "end_time")
    public long endTime;

    @ColumnInfo(name = "value")
    public long value;

}

package com.binkery.itarget.sqlite;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
@Dao
public interface TargetDao {

    @Insert
    void insertTarget(TargetEntity entity);

    @Update
    void updateTarget(TargetEntity entity);

    @Delete
    void deteteTarget(TargetEntity entity);

    @Query("select * from targets")
    List<TargetEntity> queryAllTarget();

    @Query("select * from targets where _id == :id")
    TargetEntity queryTargetById(int id);

}

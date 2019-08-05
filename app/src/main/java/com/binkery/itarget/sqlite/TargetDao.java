package com.binkery.itarget.sqlite;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
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

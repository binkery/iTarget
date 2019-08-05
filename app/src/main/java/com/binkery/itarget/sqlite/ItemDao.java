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
public interface ItemDao {

    @Insert
    void insertItem(ItemEntity entity);

    @Update
    void updateItem(ItemEntity entity);

    @Delete
    void deteteItem(ItemEntity entity);

    @Query("select * from items")
    List<ItemEntity> queryAllItem();

    @Query("select * from items where target_id = :id order by start_time DESC")
    List<ItemEntity> queryItemByTargetId(int id);

    @Query("select * from items where target_id = :id AND end_time = 0")
    ItemEntity queryItemEndTimeNull(int id);

    @Query("select * from items where _id = :id")
    ItemEntity queryItemById(int id);
}

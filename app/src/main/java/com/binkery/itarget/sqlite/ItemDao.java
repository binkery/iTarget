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
public interface ItemDao {

    @Insert
    void insertItem(ItemEntity entity);

    @Update
    void updateItem(ItemEntity entity);

    @Delete
    void deleteItem(ItemEntity entity);

    @Query("select * from items")
    List<ItemEntity> queryAllItem();

    @Query("select * from items where target_id = :id order by start_time DESC")
    List<ItemEntity> queryItemByTargetId(int id);

    @Query("select * from items where target_id =:id and (start_time between :startTime and :endTime) order by start_time DESC")
    List<ItemEntity> queryOneDayItemsByTargetId(int id, long startTime, long endTime);

    @Query("select * from items where target_id = :id AND end_time = 0")
    ItemEntity queryItemEndTimeNull(int id);

    @Query("select * from items where _id = :id")
    ItemEntity queryItemById(int id);
}

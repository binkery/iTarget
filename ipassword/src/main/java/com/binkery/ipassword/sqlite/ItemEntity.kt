package com.binkery.ipassword.sqlite

import android.content.Context
import androidx.room.*

@Entity(tableName = "items")
class ItemEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "password")
    var password: String = ""

    @ColumnInfo(name = "comments")
    var comments: String = ""

}

@Dao
interface ItemDao {

    @Insert
    fun insert(item: ItemEntity)

    @Update
    fun update(item: ItemEntity)

    @Delete
    fun delete(item: ItemEntity)

    @Query("select * from items")
    fun queryAll(): List<ItemEntity>

    @Query("select * from items where _id = :id")
    fun queryById(id: Int): ItemEntity


}

@Database(entities = arrayOf(ItemEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}

class DBHelper private constructor() {

    companion object {
        val instance: DBHelper = Holder.instance
    }

    private object Holder {
        val instance = DBHelper()
    }

    private var mAppDatabase: AppDatabase? = null

    fun init(context: Context) {
        mAppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "ipassword")
                .allowMainThreadQueries()
                .build()
    }

    fun itemDao(): ItemDao {
        return mAppDatabase?.itemDao()!!
    }
}
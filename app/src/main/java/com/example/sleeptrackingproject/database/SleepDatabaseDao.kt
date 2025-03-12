package com.example.sleeptrackingproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(night : SleepNight)

    @Update
    fun update(night: SleepNight)

    @Query("select * from daily_sleep_quality_table where nightId= :key")
    fun get(key : Long) : SleepNight

    @Query("delete from daily_sleep_quality_table")
    fun clear()

    @Query("select * from daily_sleep_quality_table order by nightId desc")
    fun getAllNights() : LiveData<List<SleepNight>>

    @Query("select * from daily_sleep_quality_table order by nightId desc limit 1")
    fun getTonight() : SleepNight?

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>
}
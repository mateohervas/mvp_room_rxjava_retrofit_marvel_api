package com.example.mateo.mateohervas_training.data.cache.Dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.example.mateo.mateohervas_training.data.cache.Entities.EventEntity

@Dao
interface EventDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventEntity)

    @Query("Select * from event")
    fun getFavoriteEvents(): List<EventEntity>

    @Query("Select * from event")
    fun getFavoriteDataSourceEvents(): DataSource.Factory<Int,EventEntity>

    @Delete
    fun deleteEvent(event: EventEntity)
}
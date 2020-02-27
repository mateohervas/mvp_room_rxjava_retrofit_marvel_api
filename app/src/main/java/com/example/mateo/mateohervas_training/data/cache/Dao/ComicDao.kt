package com.example.mateo.mateohervas_training.data.cache.Dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.example.mateo.mateohervas_training.data.cache.Entities.ComicEntity

@Dao
interface ComicDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComic(comic: ComicEntity)

    @Query("Select * from comic")
    fun getFavoriteComics(): List<ComicEntity>

    @Query("Select * from comic")
    fun getFavoriteDataSourceCharacters(): DataSource.Factory<Int,ComicEntity>

    @Delete
    fun deleteComic(comic: ComicEntity)
}
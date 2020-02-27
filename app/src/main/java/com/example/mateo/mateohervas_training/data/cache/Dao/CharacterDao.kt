package com.example.mateo.mateohervas_training.data.cache.Dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.example.mateo.mateohervas_training.data.cache.Entities.CharacterEntity

@Dao
interface CharacterDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterEntity)

    @Query("Select * from character")
    fun getFavoriteCharacters(): List<CharacterEntity>

    @Query("Select * from character")
    fun getFavoriteDataSourceCharacters(): DataSource.Factory<Int,CharacterEntity>

    @Delete
    fun deleteCharacter(character: CharacterEntity)
}
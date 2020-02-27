package com.example.mateo.mateohervas_training.data.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.mateo.mateohervas_training.data.cache.Dao.CharacterDao
import com.example.mateo.mateohervas_training.data.cache.Dao.ComicDao
import com.example.mateo.mateohervas_training.data.cache.Dao.EventDao
import com.example.mateo.mateohervas_training.data.cache.Entities.CharacterEntity
import com.example.mateo.mateohervas_training.data.cache.Entities.ComicEntity
import com.example.mateo.mateohervas_training.data.cache.Entities.EventEntity

@Database(entities = [ComicEntity::class, EventEntity::class, CharacterEntity::class], version = 3)
abstract class AppDb: RoomDatabase(){

    abstract fun ComicDao(): ComicDao
    abstract fun EventDao() : EventDao
    abstract fun CharacterDao() : CharacterDao

    companion object {

        var INSTANCE: AppDb? = null
        fun getAppDataBase(context: Context): AppDb? {
            if (INSTANCE == null){
                synchronized(AppDb::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDb::class.java, "marvelDB").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }

}
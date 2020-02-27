package com.example.mateo.mateohervas_training.data.cache.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "event")
class EventEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "thumbnail")
    var thumbnail: String = "",
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false): Serializable
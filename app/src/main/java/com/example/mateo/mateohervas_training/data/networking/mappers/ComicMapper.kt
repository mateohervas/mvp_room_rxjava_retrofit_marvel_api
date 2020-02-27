package com.example.mateo.mateohervas_training.data.networking.mappers

import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Thumbnail
import com.example.mateo.mateohervas_training.data.cache.Entities.ComicEntity

class ComicMapper{

    companion object {

        fun getComicfromComicEntity(comicEntity:  ComicEntity): Comic {

            val url = comicEntity.thumbnail
            val splitter = "/standard_amazing"
            val path = url.split(splitter)

            val comic = Comic(
                comicEntity.id,
                comicEntity.title,
                comicEntity.description,
                Thumbnail(path[0], "jpg"),
                comicEntity.isFavorite
            )

            return comic
        }

    }
}
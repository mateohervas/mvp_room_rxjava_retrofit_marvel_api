package com.example.mateo.mateohervas_training.data.networking.mappers

import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Thumbnail
import com.example.mateo.mateohervas_training.data.cache.Entities.CharacterEntity

class CharacterMapper(){

    companion object {

        fun getCharacterFromCharacterEntity(characterEntity: CharacterEntity): Character {

            val url = characterEntity.thumbnail
            val splitter = "/standard_amazing"
            val path = url.split(splitter)

            val character = Character(
                characterEntity.id,
                characterEntity.title,
                characterEntity.description,
                Thumbnail(path[0], "jpg"),
                characterEntity.isFavorite
            )

            return character
        }

    }



}
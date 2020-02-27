package com.example.mateo.mateohervas_training.presentation.comic.Detail

import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Creator

interface ComicDetailContract{

    interface View{

        fun loadCreatorslist(creators : ArrayList<Creator>)
        fun loadCharacterslist(characters : ArrayList<Character>)
    }

    interface Presenter{

         fun getComicCreatorsList(comic: Comic?)
        fun getComicCharactersList(comic: Comic?)

    }
}
package com.example.mateo.mateohervas_training.presentation.character

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.presentation.character.Adapters.CharacterListAdapter

interface CharacterContract{

    interface View{

        fun updateList(list : PagedList<Character>)

    }
    interface Presenter{
        fun getCharacters(context: Context?)
        fun listSuscriber()
        fun listLocalSubscriber(context: Context?)
        fun getComic(listAdapter : CharacterListAdapter, position : Int): Character?


    }
}
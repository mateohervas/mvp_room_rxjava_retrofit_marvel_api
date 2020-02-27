package com.example.mateo.mateohervas_training.presentation.character.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.presentation.character.Adapters.CharacterListAdapter

interface FavoriteCharacterContract{

    interface View{

        fun updateList(list : PagedList<Character>)

    }
    interface Presenter{

        fun listSubscriber(context: Context?)
        fun getCharacter(listAdapter : CharacterListAdapter, position: Int): Character

    }
}
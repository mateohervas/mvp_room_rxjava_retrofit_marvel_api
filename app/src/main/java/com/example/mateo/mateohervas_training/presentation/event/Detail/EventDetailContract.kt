package com.example.mateo.mateohervas_training.presentation.event.Detail

import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.data.networking.models.Event

interface EventDetailContract{

    interface View{

        fun loadCreatorslist(creators : ArrayList<Creator>)
        fun loadCharacterslist(characters : ArrayList<Character>)

    }

    interface Presenter{

        fun getEventCreatorsList(event: Event?)
        fun getEventCharactersList(event: Event?)

    }
}
package com.example.mateo.mateohervas_training.presentation.event.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter

interface FavoriteEventContract{

    interface View{

        fun updateList(list: PagedList<Event>)

    }
    interface Presenter{

        fun listSubscriber(context: Context?)

        fun getEvent(listAdapter : EventListAdapter, position: Int): Event
        fun onCleared()

    }
}
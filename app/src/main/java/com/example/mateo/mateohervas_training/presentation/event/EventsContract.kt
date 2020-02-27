package com.example.mateo.mateohervas_training.presentation.event

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter

interface EventsContract{

    interface View{

        fun updateList(list : PagedList<Event>)

    }

    interface Presenter{

        fun listSuscriber()
        fun getEvent(eventListAdapter: EventListAdapter, position : Int): Event?
        fun listLocalSubscriber(context: Context?)
        fun getEvents(context: Context?)
        fun onCleared()

    }
}
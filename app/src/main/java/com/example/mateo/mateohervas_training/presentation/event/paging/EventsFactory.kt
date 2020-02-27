package com.example.mateo.mateohervas_training.presentation.event.paging

import android.arch.paging.DataSource
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import io.reactivex.disposables.CompositeDisposable

class EventsFactory(private val composite : CompositeDisposable,
                    private val marvelAPI: MarvelAPI
) : DataSource.Factory<Int, Event>(){

    override fun create(): DataSource<Int, Event> {
        return EventsDataSource(marvelAPI,composite)
    }

}
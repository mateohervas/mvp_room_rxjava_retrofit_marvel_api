package com.example.mateo.mateohervas_training.presentation.event.paging

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.utils.FIRST_ADJACENT_PAGE
import com.example.mateo.mateohervas_training.utils.INITIAL_PAGE
import io.reactivex.disposables.CompositeDisposable

class EventsDataSource(val marvelAPI: MarvelAPI, private val compositeDisposable: CompositeDisposable):
    PageKeyedDataSource<Int, Event>(){

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Event>) {

        val numberOfItems = params.requestedLoadSize
        CreatingObservable(
            INITIAL_PAGE,
            FIRST_ADJACENT_PAGE, numberOfItems, callback, null
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {

        val page = params.key
        val numberOfItems = params.requestedLoadSize
        CreatingObservable(page, page + 1, numberOfItems, null, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {


        val page = params.key
        val numberOfItems = params.requestedLoadSize
        CreatingObservable(page, page - 1, numberOfItems, null, callback)
    }

    fun CreatingObservable(
        requestedPage: Int,
        adjacentPage: Int,
        requestedLoadSize: Int,
        initialCallback: LoadInitialCallback<Int, Event>?,
        callback: LoadCallback<Int, Event>?){

        compositeDisposable.add(
            marvelAPI.allEvents(requestedPage*requestedLoadSize)
                .subscribe({response ->
                    initialCallback?.onResult(response.data.results, null, adjacentPage)
                    callback?.onResult(response.data.results, adjacentPage)
                },
                    {
                        Log.e("Comics creation", "Error loading page: $requestedPage")
                    })
        )
    }
}
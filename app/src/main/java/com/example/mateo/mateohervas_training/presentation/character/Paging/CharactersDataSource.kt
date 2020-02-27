package com.example.mateo.mateohervas_training.presentation.character.Paging

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.example.mateo.mateohervas_training.utils.FIRST_ADJACENT_PAGE
import com.example.mateo.mateohervas_training.utils.INITIAL_PAGE
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import io.reactivex.disposables.CompositeDisposable
import com.example.mateo.mateohervas_training.data.networking.models.Character


class CharactersDataSource (
    private val marvelAPI: MarvelAPI,
    private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Character>() {


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>) {

        val numberOfItems = params.requestedLoadSize
        CreatingObservable(
            INITIAL_PAGE,
            FIRST_ADJACENT_PAGE, numberOfItems, callback, null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        CreatingObservable(page, page + 1, numberOfItems, null, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        CreatingObservable(page, page - 1, numberOfItems, null, callback)
    }

    fun CreatingObservable(
        requestedPage: Int,
        adjacentPage: Int,
        requestedLoadSize: Int,
        initialCallback: LoadInitialCallback<Int, Character>?,
        callback: LoadCallback<Int, Character>?){

        compositeDisposable.add(
            marvelAPI.allCharacters(requestedPage * requestedLoadSize)
                .subscribe({ response -> initialCallback?.onResult(response.data.results, null, adjacentPage)
                    callback?.onResult(response.data.results, adjacentPage)

                },
                {
                    Log.e("Characters creation", "Error loading page: $requestedPage")
                })

        )



    }

}
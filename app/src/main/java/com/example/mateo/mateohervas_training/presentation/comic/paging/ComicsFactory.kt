package com.example.mateo.mateohervas_training.presentation.comic.paging

import android.arch.paging.DataSource
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import io.reactivex.disposables.CompositeDisposable

class ComicsFactory(
    private val composite : CompositeDisposable,
    private val marvelAPI: MarvelAPI
) : DataSource.Factory<Int, Comic>(){


    override fun create(): DataSource<Int, Comic> {

        return ComicsDataSource(marvelAPI,composite)
    }


}
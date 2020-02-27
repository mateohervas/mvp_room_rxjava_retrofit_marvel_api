package com.example.mateo.mateohervas_training.presentation.character.Paging

import android.arch.paging.DataSource
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import io.reactivex.disposables.CompositeDisposable


class CharactersFactory(
     private val composite : CompositeDisposable,
     private val marvelAPI: MarvelAPI
) : DataSource.Factory<Int, Character>(){


    override fun create(): DataSource<Int, Character>{
        return CharactersDataSource(marvelAPI, composite)
    }


}
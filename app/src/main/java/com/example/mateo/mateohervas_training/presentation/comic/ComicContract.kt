package com.example.mateo.mateohervas_training.presentation.comic

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter

interface ComicContract{

    interface View{

        fun updateList(list : PagedList<Comic>)
    }

    interface Presenter{

        fun listSuscriber()
        fun getComic(listAdapter : ComicListAdapter, position : Int): Comic?
        fun getComics(context: Context?)
        fun listLocalSubscriber(context: Context?)

    }
}
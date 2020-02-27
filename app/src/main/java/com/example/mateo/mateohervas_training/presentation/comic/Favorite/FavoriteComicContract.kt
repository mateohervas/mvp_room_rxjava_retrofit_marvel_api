package com.example.mateo.mateohervas_training.presentation.comic.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter

interface FavoriteComicContract{

    interface View{

        fun updateList(list: PagedList<Comic>)

    }
    interface Presenter{

        fun listSubscriber(context: Context?)
        fun getComic(listAdapter : ComicListAdapter, position: Int): Comic

    }
}
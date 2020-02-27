package com.example.mateo.mateohervas_training.presentation.comic.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.cache.Dao.ComicDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.mappers.ComicMapper
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class FavoriteComicPresenter(val FavoritComicView : FavoriteComicContract.View): FavoriteComicContract.Presenter{

    private var db: AppDb? = null
    private var comicDao : ComicDao? = null


    override fun listSubscriber(context: Context?) {

            Observable.fromCallable {
                db = AppDb.getAppDataBase(context!!.applicationContext)
                comicDao = db?.ComicDao()
                comicDao?.getFavoriteComics()

                val myConfig = PagedList.Config.Builder()
                    .setInitialLoadSizeHint(10)
                    .setPageSize(20) .build()

                val comicDataSource = comicDao?.getFavoriteDataSourceCharacters()!!.map { ComicMapper.getComicfromComicEntity(it)}
                val pagedList = PagedList.Builder<Int, Comic>(comicDataSource.create(),myConfig)

                pagedList
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .build()


            }.doOnNext{
                    list -> FavoritComicView.updateList(list)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()


    }

    override fun getComic(listAdapter: ComicListAdapter, position: Int): Comic {

        val comic: Comic = listAdapter.getItemAtPosition(position)!!

        return comic
    }

}
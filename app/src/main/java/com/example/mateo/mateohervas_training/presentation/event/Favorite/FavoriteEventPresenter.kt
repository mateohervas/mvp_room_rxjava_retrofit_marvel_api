package com.example.mateo.mateohervas_training.presentation.event.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.cache.Dao.EventDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.mappers.EventMapper
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors


class FavoriteEventPresenter(val favoriteEventView : FavoriteEventContract.View): FavoriteEventContract.Presenter{


    private var db: AppDb? = null
    private var eventDao : EventDao? = null
    var disposable : Disposable? = null

    override fun listSubscriber(context: Context?) {

        Observable.fromCallable {
            db = AppDb.getAppDataBase(context!!.applicationContext)
            eventDao = db?.EventDao()
            eventDao?.getFavoriteEvents()

            val myConfig = PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(20) .build()

            val eventDataSource = eventDao?.getFavoriteDataSourceEvents()!!.map { EventMapper.getEventfromEventEntity(it)}
            val pagedList = PagedList.Builder<Int, Event>(eventDataSource.create(),myConfig)

            pagedList
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor(Executors.newSingleThreadExecutor())
                .build()

        }.doOnNext{
                list -> favoriteEventView.updateList(list)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()


    }

    override fun getEvent(listAdapter: EventListAdapter, position: Int): Event {

        val event  = listAdapter.getItemAtPosition(position)!!

        return event
    }

    override fun onCleared() {

        disposable?.dispose()
    }

}
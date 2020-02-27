package com.example.mateo.mateohervas_training.presentation.event

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.content.Context
import android.util.Log
import com.example.mateo.mateohervas_training.data.cache.Dao.EventDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.data.cache.Entities.EventEntity
import com.example.mateo.mateohervas_training.utils.INITIAL_LOAD_SIZE_HINT
import com.example.mateo.mateohervas_training.utils.PAGE_SIZE
import com.example.mateo.mateohervas_training.utils.PREFETCH_DISTANCE
import com.example.mateo.mateohervas_training.presentation.event.Adapters.EventListAdapter
import com.example.mateo.mateohervas_training.presentation.event.paging.EventsFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventsPresenter(val eventsFragment : EventsContract.View): EventsContract.Presenter{

    val events : Observable<PagedList<Event>>
    private val source : EventsFactory
    private val composite = CompositeDisposable()
     var disposable: Disposable? = null

    private var localEvents : MutableList<EventEntity?> = ArrayList<EventEntity?>()

    init {
        source = EventsFactory(composite, MarvelAPI.getService())

        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

        events = RxPagedListBuilder(source,config)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
            .cache()
    }

    override fun getEvents(context: Context?) {
        listLocalSubscriber(context)
    }
    override fun listSuscriber() {

        disposable = events
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                    list-> markAsFavortie(list)
            },{
                    e-> Log.e("List Subscriber","Error:  $e")
            })
    }

    override fun getEvent(eventListAdapter: EventListAdapter, position: Int): Event? {

        val event: Event? = eventListAdapter.let {
            it.currentList?.get(position)
        }

        return event
    }
    private fun markAsFavortie(list:PagedList<Event> ){
        var i = 0

        for (event in list){
            if (localEvents.size > 0){
                var pos = 0
                for (localEvent in localEvents){
                    if(event.id == localEvent?.id){
                        list[i]?.isFavorite = true
                    }
                    pos++
                }
                i++
            }else{
                break
            }

        }
        eventsFragment.updateList(list)
    }

    override fun listLocalSubscriber(context: Context?) {

        Observable.fromCallable {

            val db: AppDb? = AppDb.getAppDataBase(context!!.applicationContext)
            val eventDao : EventDao? = db?.EventDao()
            eventDao?.getFavoriteEvents()

        }.doOnNext{
                list ->  localEvents.addAll(list)
            listSuscriber()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onCleared() {

        composite.clear()
        disposable?.dispose()

    }



}
package com.example.mateo.mateohervas_training.presentation.comic

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.content.Context
import android.util.Log
import com.example.mateo.mateohervas_training.data.cache.Dao.ComicDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.data.cache.Entities.ComicEntity
import com.example.mateo.mateohervas_training.utils.INITIAL_LOAD_SIZE_HINT
import com.example.mateo.mateohervas_training.utils.PAGE_SIZE
import com.example.mateo.mateohervas_training.utils.PREFETCH_DISTANCE
import com.example.mateo.mateohervas_training.presentation.comic.Adapters.ComicListAdapter
import com.example.mateo.mateohervas_training.presentation.comic.paging.ComicsFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ComicsPresenter( val comicsFragment : ComicContract.View) : ComicContract.Presenter{


    val comics: Observable<PagedList<Comic>>
    private val composite = CompositeDisposable()
    private val source: ComicsFactory

    private lateinit var disposable: Disposable

    private var localComics : MutableList<ComicEntity?> = ArrayList<ComicEntity?>()


    init {
        source = ComicsFactory(composite, MarvelAPI.getService())

        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

        comics = RxPagedListBuilder(source,config)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
            .cache()
    }

    override fun getComics(context: Context?) {
        listLocalSubscriber(context)
    }


    override fun listSuscriber() {

        disposable = comics
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                   list-> markAsFavortie(list)
            },{
                    e-> Log.e("Comic Presenter"," LIST SUBSCRIBER Error:  $e")
            })
    }
    override fun getComic(listAdapter : ComicListAdapter, position : Int) : Comic?{

        val comic : Comic? = listAdapter.let {
            it.currentList?.get(position)
        }

        return comic
    }

    private fun markAsFavortie(list:PagedList<Comic> ){
        var i = 0

        for (comic in list){
            if (localComics.size > 0){
                var pos = 0
                for (localComic in localComics){
                    if(comic.id == localComic?.id){
                        list[i]?.isFavorite = true
                    }
                    pos++
                }
                i++
            }else{
                break
            }

        }

        comicsFragment.updateList(list)
    }

    override fun listLocalSubscriber(context: Context?) {

        Observable.fromCallable {

            val db: AppDb? = AppDb.getAppDataBase(context!!.applicationContext)
            val comicDao : ComicDao? = db?.ComicDao()
            comicDao?.getFavoriteComics()

        }.doOnNext{
                list ->  localComics.addAll(list)
            listSuscriber()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }


    fun onCleared(){

        composite.clear()
        disposable.dispose()
    }
}
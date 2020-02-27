package com.example.mateo.mateohervas_training.presentation.character

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.content.Context
import android.util.Log
import com.example.mateo.mateohervas_training.data.cache.Dao.CharacterDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.utils.INITIAL_LOAD_SIZE_HINT
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.data.cache.Entities.CharacterEntity
import com.example.mateo.mateohervas_training.utils.PAGE_SIZE
import com.example.mateo.mateohervas_training.utils.PREFETCH_DISTANCE
import com.example.mateo.mateohervas_training.presentation.character.Adapters.CharacterListAdapter
import com.example.mateo.mateohervas_training.presentation.character.Paging.CharactersFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CharactersPresenter(val charactersFragment: CharacterContract.View): CharacterContract.Presenter{

    override fun getComic(listAdapter: CharacterListAdapter, position: Int): Character? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    var characters: Observable<PagedList<Character>>
    private val composite = CompositeDisposable()
    private  val source: CharactersFactory
    var disposable: Disposable? = null

    private var localCharactersLocal : MutableList<CharacterEntity?> = ArrayList<CharacterEntity?>()


    init {
        source = CharactersFactory(composite, MarvelAPI.getService())

        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

        characters = RxPagedListBuilder(source, config)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
            .cache()
    }

    override fun getCharacters(context: Context?){
        listLocalSubscriber(context)
    }

    override fun listSuscriber() {

        disposable = characters
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    list-> markAsFavortie(list)
            },{
                    e-> Log.e("CharactersPresenter ","List Subscriber Error:  $e")
            })
    }

    private fun markAsFavortie(list:PagedList<Character> ){
        var i = 0

        for (character in list){
            if (localCharactersLocal.size > 0){
                var pos = 0
                for (localCharacter in localCharactersLocal){
                    if(character.id == localCharacter?.id){
                        list[i]?.isFavorite = true
                    }
                    pos++
                }
                i++
            }else{
                break
            }
        }
        charactersFragment.updateList(list)
    }

    override fun listLocalSubscriber(context: Context?) {

        Observable.fromCallable {

            val db: AppDb? = AppDb.getAppDataBase(context!!.applicationContext)
            val characterDao : CharacterDao? = db?.CharacterDao()
            characterDao?.getFavoriteCharacters()

        }.doOnNext{
                list ->  localCharactersLocal.addAll(list)
                listSuscriber()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }


     fun onCleared() {
        composite.clear()
         disposable?.dispose()
    }

}
package com.example.mateo.mateohervas_training.presentation.character.Favorite

import android.arch.paging.PagedList
import android.content.Context
import com.example.mateo.mateohervas_training.data.cache.Dao.CharacterDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.mappers.CharacterMapper
import com.example.mateo.mateohervas_training.presentation.character.Adapters.CharacterListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors


class FavoriteCharacterPresenter(val favoriteCharacterView : FavoriteCharacterContract.View): FavoriteCharacterContract.Presenter{


    private var db: AppDb? = null
    private var characterDao : CharacterDao? = null


    override fun listSubscriber(context: Context?) {
            Observable.fromCallable {
                db = AppDb.getAppDataBase(context!!.applicationContext)
                characterDao = db?.CharacterDao()
                characterDao?.getFavoriteDataSourceCharacters()!!.map { CharacterMapper.getCharacterFromCharacterEntity(it)}


                val myConfig = PagedList.Config.Builder()
                    .setInitialLoadSizeHint(10)
                    .setPageSize(20) .build()

                val characterDataSource = characterDao?.getFavoriteDataSourceCharacters()!!.map { CharacterMapper.getCharacterFromCharacterEntity(it)}
                val pagedList = PagedList.Builder<Int, Character>(characterDataSource.create(),myConfig)

                pagedList
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .build()


            }.doOnNext{
                    list -> favoriteCharacterView.updateList(list)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun getCharacter(listAdapter: CharacterListAdapter, position: Int): Character {

        val character = listAdapter.getItemAtPosition(position)

        return character!!
    }


}
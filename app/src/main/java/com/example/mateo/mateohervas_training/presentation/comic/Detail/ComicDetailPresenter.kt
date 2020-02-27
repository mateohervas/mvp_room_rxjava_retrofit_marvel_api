package com.example.mateo.mateohervas_training.presentation.comic.Detail

import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.utils.Response
import retrofit2.Call
import retrofit2.Callback

class ComicDetailPresenter(val comicDetailActivity : ComicDetailContract.View): ComicDetailContract.Presenter{



    var creatorList : ArrayList<Creator> = ArrayList()
    var characterList : ArrayList<Character> = ArrayList()

    override fun getComicCreatorsList(comic: Comic?){

        val call = MarvelAPI.getService().creatorsFromComic(comic!!.id)
        call.enqueue(object : Callback<Response<Creator>> {

            override fun onResponse(call: Call<Response<Creator>>, response: retrofit2.Response<Response<Creator>>){
                response.body()?.data?.let {
                    creatorList.addAll(it.results as ArrayList<Creator>)
                    comicDetailActivity.loadCreatorslist(creatorList)
                }
            }
            override fun onFailure(call: Call<Response<Creator>>, t: Throwable) {
            }
        })

    }

    override fun getComicCharactersList(comic: Comic?) {

        val call = MarvelAPI.getService().charactersFromComic(comic!!.id)
        call.enqueue(object : Callback<Response<Character>>{

            override fun onResponse(call: Call<Response<Character>>, response: retrofit2.Response<Response<Character>>){
                response.body()?.data?.let {
                    characterList = it.results as ArrayList<Character>
                    comicDetailActivity.loadCharacterslist(characterList)

                }
            }
            override fun onFailure(call: Call<Response<Character>>, t: Throwable) {}
        })
    }

}
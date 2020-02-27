package com.example.mateo.mateohervas_training.presentation.event.Detail

import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.networking.MarvelAPI
import com.example.mateo.mateohervas_training.utils.Response
import retrofit2.Call
import retrofit2.Callback

class EventDetailPresenter(val eventDetailActivity: EventDetailContract.View): EventDetailContract.Presenter{

    var creatorList : ArrayList<Creator> = ArrayList()
    var characterList : ArrayList<Character> = ArrayList()

    override fun getEventCreatorsList(event: Event?) {

        val call = MarvelAPI.getService().creatorsFromComic(event!!.id)
        call.enqueue(object : Callback<Response<Creator>> {

            override fun onResponse(call: Call<Response<Creator>>, response: retrofit2.Response<Response<Creator>>){
                response.body()?.data?.let {
                    creatorList.addAll(it.results as ArrayList<Creator>)
                    eventDetailActivity.loadCreatorslist(creatorList)
                }
            }
            override fun onFailure(call: Call<Response<Creator>>, t: Throwable) {
            }
        })
    }

    override fun getEventCharactersList(event: Event?) {

        val call = MarvelAPI.getService().charactersFromComic(event!!.id)
        call.enqueue(object : Callback<Response<Character>>{

            override fun onResponse(call: Call<Response<Character>>, response: retrofit2.Response<Response<Character>>){
                response.body()?.data?.let {
                    characterList = it.results as ArrayList<Character>
                    eventDetailActivity.loadCharacterslist(characterList)

                }
            }
            override fun onFailure(call: Call<Response<Character>>, t: Throwable) {}
        })
    }


}
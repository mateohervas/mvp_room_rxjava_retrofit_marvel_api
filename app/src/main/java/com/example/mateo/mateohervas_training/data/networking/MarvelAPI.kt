package com.example.mateo.mateohervas_training.data.networking


import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.utils.*
import com.google.gson.GsonBuilder

import io.reactivex.Observable

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface MarvelAPI {

    @GET("characters")
    fun allCharacters(@Query("offset") offset: Int? = 0): Observable<Response<Character>>

    @GET("comics")
    fun allComics(@Query("offset") offset: Int? = 0): Observable<Response<Comic>>

    @GET("comics/{comicId}/creators")
    fun creatorsFromComic(@Path ("comicId") comicId: Int) : Call<Response<Creator>>

    @GET("comics/{comicId}/characters")
    fun charactersFromComic(@Path ("comicId") comicId: Int) : Call<Response<Character>>

    @GET("events")
    fun allEvents(@Query("offset") offset: Int? = 0): Observable<Response<Event>>

    @GET("events/{eventId}/creators")
    fun creatorsFromEvent(@Path ("eventId") comicId: Int) : Call<Response<Creator>>

    @GET("events/{eventId}/characters")
    fun charactersFromEvent(@Path ("eventId") comicId: Int) : Call<Response<Character>>



    companion object {



        fun getService(): MarvelAPI {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            httpClient.addInterceptor{ chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val timeStamp = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis / 1000L).toString()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("ts", timeStamp)
                    .addQueryParameter("hash", "$timeStamp$PRIVATE_KEY$API_KEY".md5())
                    .build()
                chain.proceed(original.newBuilder().url(url).build())
            }

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://gateway.marvel.com/v1/public/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

            return retrofit.create<MarvelAPI>(MarvelAPI::class.java)
        }



    }
}
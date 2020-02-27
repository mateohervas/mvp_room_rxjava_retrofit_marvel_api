package com.example.mateo.mateohervas_training.presentation.event.Detail

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mateo.mateohervas_training.data.cache.Dao.EventDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.data.networking.models.Event
import com.example.mateo.mateohervas_training.data.cache.Entities.EventEntity
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.EVENT_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.utils.load
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.example.mateo.mateohervas_training.presentation.comic.Detail.Adapters.ComicCharactersListAdapter
import com.example.mateo.mateohervas_training.presentation.comic.Detail.Adapters.ComicCreatorsListAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comic_and_events_detail.*

class EventDetailActivity(): AppCompatActivity(), EventDetailContract.View{

    var creatorList : ArrayList<Creator> = ArrayList()
    var characterList: ArrayList<Character> = ArrayList()
    val eventDetailPresenter = EventDetailPresenter(this)

    private var db: AppDb? = null
    private var eventDao : EventDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_comic_and_events_detail)
        val event = createEventFromIntent()

        setSupportActionBar(comicToolbar)

        if(supportActionBar!=null) {

            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            txtComicTitle.setText(event.title)
            txtComicName.text = event.title
            app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    //  Collapsed
                    txtComicName.visibility = View.GONE
                    txtComicTitle.visibility = View.VISIBLE
                    tglFavorite.visibility = View.INVISIBLE


                } else {
                    txtComicTitle.visibility = View.INVISIBLE
                    //Expanded
                    txtComicName.visibility = View.VISIBLE
                    tglFavorite.visibility = View.VISIBLE
                }
            })

        }

        updateScreen(event)
        onFavoriteSelected(event)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.getItemId() == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun updateScreen(event: Event?){

        tglFavorite.isChecked = event!!.isFavorite
        this.let {
            event?.let { event -> bindEventInformation(event) }
        }
        eventDetailPresenter.getEventCharactersList(event)
        eventDetailPresenter.getEventCreatorsList(event)

    }

    fun onFavoriteSelected(event: Event) {

        tglFavorite.setOnClickListener {

            val eventEntity = EventEntity()
            eventEntity.id = event.id
            eventEntity.title = event.title
            eventEntity.description = event._details
            eventEntity.thumbnail = event.thumbnail.getUrl()

            event.isFavorite = tglFavorite.isChecked
            db = AppDb.getAppDataBase(context = this)
            Observable.fromCallable {
                eventDao = db?.EventDao()
                if (event.isFavorite) {
                    eventDao?.insertEvent(eventEntity)

                }else{
                    eventDao?.deleteEvent(eventEntity)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe{
                        e-> Log.e("List Subscriber","Error:  $e")
                    if(tglFavorite.isChecked) {
                        Toast.makeText(this, getString(R.string.event_saved), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this,getString(R.string.event_deleted), Toast.LENGTH_SHORT).show()
                    }

                    RxBus.publish(RxEvent.EventAddEvent(event))
                    RxBus.publish(RxEvent.EventEventStateChanged(event))

                }
        }
    }

    fun createEventFromIntent(): Event {
        val i : Intent = getIntent()
        val bundle = i.getBundleExtra(EVENT_KEY)
        val event = bundle.getSerializable(EVENT_KEY) as Event

        val transitionName = i.getStringExtra(TRANSITION_NAME)
        imgComicEventBanner.transitionName = transitionName

        return event

    }

    private fun bindEventInformation(event: Event) {

        with(event){

            Picasso.with(applicationContext)
                .load(thumbnail.getUrl())
                .resize(2000,1000)
                .centerCrop()
                .noFade()
                .into(imgComicEventBanner, object : Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError() {
                        supportStartPostponedEnterTransition()
                    }
                })
            imgComicEventBanner.load(thumbnail.getUrl())
            txtComicDetail.text = event._details

        }
    }

    override fun loadCreatorslist(creators: ArrayList<Creator>) {

        creatorList.addAll(creators)
        setUpCreatorRecyclerView(rclComicCreator)
    }

    override fun loadCharacterslist(characters: ArrayList<Character>) {

        characterList.addAll(characters)
        setUpCharactersRecyclerView(rclComicCharacters)
    }

    fun setUpCreatorRecyclerView(recycler: RecyclerView){

        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager =layoutManager

        val creatorsAdapter =
            ComicCreatorsListAdapter(creatorList)
        recycler.adapter = creatorsAdapter
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        prgCreators.visibility = View.GONE

    }

    private fun setUpCharactersRecyclerView(recycler: RecyclerView){

        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        val charactersAdapter =
            ComicCharactersListAdapter(characterList)
        recycler.adapter = charactersAdapter
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        prgCharacters.visibility = View.GONE
    }
}
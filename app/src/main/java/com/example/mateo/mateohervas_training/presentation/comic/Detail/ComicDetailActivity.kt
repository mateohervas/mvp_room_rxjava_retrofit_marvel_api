package com.example.mateo.mateohervas_training.presentation.comic.Detail

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
import com.example.mateo.mateohervas_training.data.cache.Dao.ComicDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.data.cache.Entities.ComicEntity
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.COMIC_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
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

class ComicDetailActivity() : AppCompatActivity(), ComicDetailContract.View{


    var creatorList : ArrayList<Creator> = ArrayList()
    var characterList: ArrayList<Character> = ArrayList()
    val comicDetailPresenter = ComicDetailPresenter(this)
    private var db: AppDb? = null
    private var comicDao : ComicDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_comic_and_events_detail)
        val comic = createComicFromIntent()

        setSupportActionBar(comicToolbar)

        if(supportActionBar!=null) {

            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            txtComicTitle.setText(comic.title)
            txtComicName.text = comic.title

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


        updateScreen(comic)
        onFavoriteSelected(comic)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.getItemId() == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateScreen(comic: Comic?){

        tglFavorite.isChecked = comic!!.isFavorite
        this.let {
            comic.let { comic -> bindComicInformation(comic) }
        }
        comicDetailPresenter.getComicCreatorsList(comic)
        comicDetailPresenter.getComicCharactersList(comic)

    }
    fun createComicFromIntent(): Comic {
        val i :Intent = getIntent()
        val bundle = i.getBundleExtra(COMIC_KEY)
        val comic = bundle.getSerializable(COMIC_KEY) as Comic
        val transitionName = i.getStringExtra(TRANSITION_NAME)
        imgComicEventBanner.transitionName = transitionName

        return comic

    }

    fun onFavoriteSelected(comic: Comic) {


        tglFavorite.setOnClickListener {

            val comicEntity = ComicEntity()
            comicEntity.id = comic.id
            comicEntity.title = comic.title
            comicEntity.description = comic._details
            comicEntity.thumbnail = comic.thumbnail.getUrl()

            comic.isFavorite = tglFavorite.isChecked
            db = AppDb.getAppDataBase(context = this)
            Observable.fromCallable {
                comicDao = db?.ComicDao()
                if (comic.isFavorite) {
                    comicDao?.insertComic(comicEntity)

                }else{
                    comicDao?.deleteComic(comicEntity)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe{
                        e-> Log.e("List Subscriber","Error:  $e")

                    if(tglFavorite.isChecked) {
                        Toast.makeText(this, getString(R.string.comic_saved), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this,getString(R.string.comid_deleted), Toast.LENGTH_SHORT).show()
                    }
                    RxBus.publish(RxEvent.EventAddComic(comic))
                    RxBus.publish(RxEvent.EventComicStateChanged(comic))
                }
        }
    }

    private fun bindComicInformation(comic: Comic) {

        with(comic){

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

            txtComicDetail.text = comic._details

        }
    }

    override fun loadCreatorslist(creators : ArrayList<Creator>) {

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

        recycler.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL))

        prgCreators.visibility = View.GONE

    }

    private fun setUpCharactersRecyclerView(recycler: RecyclerView){

        val layoutManager =LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        val charactersAdapter =
            ComicCharactersListAdapter(characterList)
        recycler.adapter = charactersAdapter
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL))
        prgCharacters.visibility = View.GONE
    }

}
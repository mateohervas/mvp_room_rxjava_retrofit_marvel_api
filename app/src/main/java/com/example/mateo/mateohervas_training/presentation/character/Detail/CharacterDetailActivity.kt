package com.example.mateo.mateohervas_training.presentation.character.Detail

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mateo.mateohervas_training.data.cache.Dao.CharacterDao
import com.example.mateo.mateohervas_training.data.cache.AppDb
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.data.cache.Entities.CharacterEntity
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.CHARACTER_KEY
import com.example.mateo.mateohervas_training.utils.TRANSITION_NAME
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxBus
import com.example.mateo.mateohervas_training.data.networking.RxBus.RxEvent
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity: AppCompatActivity(){

    private var db : AppDb? = null
    private var characterDao : CharacterDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_character_detail)
        supportPostponeEnterTransition();


        val character = createCharacterFromIntent()

        setSupportActionBar(characterToolbar)

        if(supportActionBar!=null){

            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            txtCharacterTitle.setText(character.name)
            txtCharacterName.text = character.name

            app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    //  Collapsed
                    txtCharacterName.visibility = View.GONE
                    txtCharacterTitle.visibility = View.VISIBLE
                    btnFavorite.visibility = View.INVISIBLE


                } else {
                    txtCharacterTitle.visibility = View.INVISIBLE
                    //Expanded
                    txtCharacterName.visibility = View.VISIBLE
                    btnFavorite.visibility = View.VISIBLE
                }
            })


        }


        updateScreen(character)
        onFavoriteSelected(character)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        
        if (item!!.getItemId() == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateScreen(character: Character){

        btnFavorite.isChecked = character.isFavorite
        this.let {
            character.let {character -> bindCharacterInformation(character)}
        }

    }


    fun onFavoriteSelected(character: Character) {

        btnFavorite.setOnClickListener {

            val characterEntity = CharacterEntity()
            characterEntity.id = character.id
            characterEntity.title = character.name
            characterEntity.description = character._details
            characterEntity.thumbnail = character.thumbnail.getUrl()


            character.isFavorite = btnFavorite.isChecked
            db = AppDb.getAppDataBase(context = this)
            Observable.fromCallable {
                characterDao = db?.CharacterDao()
                if (character.isFavorite) {
                    characterDao?.insertCharacter(characterEntity)
                }else{
                    characterDao?.deleteCharacter(characterEntity)

                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe{
                        e-> Log.e("List Subscriber","Error:  $e")

                    if(btnFavorite.isChecked) {
                        Toast.makeText(this, getString(R.string.character_saved), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this,getString(R.string.character_deleted), Toast.LENGTH_SHORT).show()

                    }
                    RxBus.publish(RxEvent.EventAddCharacter(character))
                    RxBus.publish(RxEvent.EventCharacterStateChanged(character))
                }
        }


    }
    fun createCharacterFromIntent() : Character {

        val i: Intent = getIntent()

        val bundle = i.getBundleExtra(CHARACTER_KEY)
        val character = bundle.getSerializable(CHARACTER_KEY) as Character
        val transitionName = i.getStringExtra(TRANSITION_NAME)
        imgCharacterBanner.transitionName = transitionName

        return character

    }

    fun bindCharacterInformation(character: Character){


        with(character){
            Picasso.with(applicationContext)
                .load(thumbnail.getUrl())
                .resize(2000,1000)
                .centerCrop()
                .noFade()
                .into(imgCharacterBanner, object :Callback{
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError() {
                        supportStartPostponedEnterTransition()
                    }
                })

            txtCharacterDetail.text= _details
        }

    }

}
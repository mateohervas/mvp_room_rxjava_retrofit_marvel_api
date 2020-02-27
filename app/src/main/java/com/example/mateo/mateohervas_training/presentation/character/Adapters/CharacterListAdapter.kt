package com.example.mateo.mateohervas_training.presentation.character.Adapters

import android.arch.paging.PagedListAdapter
import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.load
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import kotlinx.android.synthetic.main.item_recycler.view.*

class CharacterListAdapter(private val recycler : RecyclerItemClickListener) : PagedListAdapter<Character, CharacterListAdapter.ViewHolder>(characterDiff){

    fun getItemAtPosition(position: Int): Character? {
        return getItem(position)
    }

    fun onStateChanged(characterEntity: Character, isFavorite: Boolean){
        var i = 0
        for(item in currentList!!){
            if(item.id == characterEntity.id){
                currentList?.get(i)?.isFavorite = isFavorite
                notifyItemChanged(i)
            }
            i++
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)

        return CharacterListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterListAdapter.ViewHolder, pos: Int) {

        val character : Character? = getItem(pos)

        holder.name.text = character?.name
        holder.image.load("${character?.thumbnail?.path}/standard_amazing.${character?.thumbnail?.extension}")


        if(character!!.isFavorite) {
            Log.d("Mateo", " List adapter  : ${character.name} and its state: ${character.isFavorite}")
            holder.star.visibility = View.VISIBLE
        } else{
            holder.star.visibility = View.INVISIBLE
        }

        val transitionName = character.id.toString()
        ViewCompat.setTransitionName(holder.image,transitionName)

        holder.itemView.setOnClickListener{
            recycler.onRecyclerViewClick(holder.image, pos)
        }
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        val image = item.imgCard
        val name = item.txtCardName
        val star = item.imgStar
    }

    companion object {

        val characterDiff = object : DiffUtil.ItemCallback<Character>(){
            override fun areItemsTheSame(existent: Character, new: Character): Boolean {

                return existent.id == new.id
            }

            override fun areContentsTheSame(existent: Character, new: Character): Boolean {

                return existent == new
            }


        }
    }

}
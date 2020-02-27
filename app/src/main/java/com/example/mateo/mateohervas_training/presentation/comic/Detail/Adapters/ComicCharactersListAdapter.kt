package com.example.mateo.mateohervas_training.presentation.comic.Detail.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.data.networking.models.Character
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.load
import kotlinx.android.synthetic.main.item_details.view.*

class ComicCharactersListAdapter(private val characters : ArrayList<Character>): RecyclerView.Adapter<ComicCharactersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_details, viewGroup, false)

        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = characters.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val character : Character? = characters[position]

        holder?.let {
            it.bindView(character)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(character: Character?){

            val imgCharacter = itemView.imgChip
            imgCharacter.load(character!!.thumbnail.getUrl())
        }
    }


}
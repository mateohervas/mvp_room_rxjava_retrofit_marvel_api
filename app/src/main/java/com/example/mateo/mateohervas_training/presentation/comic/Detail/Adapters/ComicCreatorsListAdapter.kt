package com.example.mateo.mateohervas_training.presentation.comic.Detail.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.data.networking.models.Creator
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.load
import kotlinx.android.synthetic.main.item_details.view.*

class ComicCreatorsListAdapter(private val creators: ArrayList<Creator>): RecyclerView.Adapter<ComicCreatorsListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_details, viewGroup, false)

        return ViewHolder(
            view
        )

    }

    override fun getItemCount(): Int = creators.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val creator : Creator? = creators[position]
        holder?.let {
            it.bindView(creator)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(creator: Creator?){

            val imgCreator = itemView.imgChip
            imgCreator.load(creator!!.thumbnail.getUrl())

        }

    }


}
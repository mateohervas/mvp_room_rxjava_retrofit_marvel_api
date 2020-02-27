package com.example.mateo.mateohervas_training.presentation.comic.Adapters

import android.arch.paging.PagedListAdapter
import android.support.v4.view.ViewCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mateo.mateohervas_training.data.networking.models.Comic
import com.example.mateo.mateohervas_training.R
import com.example.mateo.mateohervas_training.utils.load
import com.example.mateo.mateohervas_training.presentation.RecyclerItemClickListener
import kotlinx.android.synthetic.main.item_recycler.view.*

class ComicListAdapter(private val recycler: RecyclerItemClickListener):PagedListAdapter<Comic, ComicListAdapter.ViewHolder>(
    comicDiff
){

    fun getItemAtPosition(position: Int): Comic? {
        return getItem(position)
    }

    fun onStateChanged(comic: Comic, isFavorite: Boolean){

        var i = 0
        for(item in currentList!!){
            if(item.id == comic.id){
                currentList?.get(i)?.isFavorite = isFavorite
                notifyItemChanged(i)
            }
            i++
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        val comic: Comic? = getItem(pos)


        holder.title.text = comic?.title

        holder.image.load("${comic?.thumbnail?.path}/standard_amazing.${comic?.thumbnail?.extension}")

        if(comic!!.isFavorite){
            holder.star.visibility = View.VISIBLE
        }else{
            holder.star.visibility = View.INVISIBLE
        }

        val transitionName = comic.id.toString()
        ViewCompat.setTransitionName(holder.image,transitionName)

        holder.itemView.setOnClickListener{

            recycler.onRecyclerViewClick(holder.image, pos)

        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){

        val image = item.imgCard
        val title = item.txtCardName
        val star = item.imgStar
    }

    companion object {
        val comicDiff = object: DiffUtil.ItemCallback<Comic>(){
            override fun areItemsTheSame(existent: Comic, new: Comic): Boolean {

                return existent.id == new.id
            }

            override fun areContentsTheSame(existent: Comic, new: Comic): Boolean {

                return existent == new
            }
        }
    }


}
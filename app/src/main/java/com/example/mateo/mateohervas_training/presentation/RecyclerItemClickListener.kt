package com.example.mateo.mateohervas_training.presentation

import android.view.View
import android.widget.ImageView

interface RecyclerItemClickListener {

    fun onRecyclerViewClick(image: ImageView, position : Int)
}
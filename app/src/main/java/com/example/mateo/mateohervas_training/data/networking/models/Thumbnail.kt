package com.example.mateo.mateohervas_training.data.networking.models

import java.io.Serializable

data class Thumbnail(
    val path: String,
    val extension: String
) : Serializable {
    fun getUrl() =  "${path}/standard_amazing.${extension}"
}
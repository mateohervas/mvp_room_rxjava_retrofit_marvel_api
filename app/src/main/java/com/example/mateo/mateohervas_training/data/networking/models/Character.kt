package com.example.mateo.mateohervas_training.data.networking.models


import java.io.Serializable

class Character (
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    var isFavorite: Boolean) : Serializable {
        val _details : String
             get() = if(description.isNullOrEmpty()){
                 "No details available for this character"
             }else{
                 description
             }



}
package com.example.mateo.mateohervas_training.data.networking.models

import java.io.Serializable

class Creator(val id: Int,val fullName : String, val thumbnail: Thumbnail):Serializable{

    val _details : String
        get() = if(fullName.isNullOrEmpty()){
            "No creators available"
        }else{

            fullName
        }
}
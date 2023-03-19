package com.example.survey2

import android.content.Context
import java.io.File

object MapsList {
    var mapIndex = mutableListOf<com.example.survey2.Map>()
    var currMapName: String = ""

    fun deleteMap(map: com.example.survey2.Map, context: Context){

        val path = context.filesDir.absolutePath

        val file = File(path+"/"+map.name+".cbg")
        val result = file.delete()

        /*for(i in 0 until mapIndex.size){
            if (mapIndex[i].name == map.name){
                mapIndex.removeAt(i)
            }
        }
        */


        if (result) {
            println(map.name+" WAS DELETED")

        } else {
            if(file.exists()){
                println("FILE FOUND BUT ")
            }
            else{
                "FILE WAS NOT FOUND SO "
            }
            println(map.name+" WAS NOT DELETED")

        }
    }
}
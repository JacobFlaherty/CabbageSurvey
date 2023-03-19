package com.example.survey2
import com.google.gson.Gson

class MapEncodeDecode {
    private val gson = Gson()

    public fun encode(map: InternalStorageMap): String {
        return gson.toJson(map)
    }

    public fun decode(encoded: String): InternalStorageMap {


        return gson.fromJson(encoded, InternalStorageMap::class.java)
    }
}
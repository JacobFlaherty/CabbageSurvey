package com.example.survey2

data class InternalStorageMap(
    val name: String,

){
    var startPointsX = mutableListOf<Float>()
    var startPointsY = mutableListOf<Float>()
    var startPointsZ = mutableListOf<Float>()

    var endPointsX = mutableListOf<Float>()
    var endPointsY = mutableListOf<Float>()
    var endPointsZ = mutableListOf<Float>()
}

package com.example.survey2

object PointsList {
    var startPoint: Point
    var currentPoint: Int
   init {
       startPoint = Point(0f,0f,0f)
       currentPoint = 0

   }
    var pointIndex = mutableListOf(startPoint)


}
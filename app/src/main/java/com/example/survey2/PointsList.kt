package com.example.survey2

object PointsList {
    var startPoint: Point
    var currentPoint: Int
   init {
       startPoint = Point(0f,0f,0f, 0)
       currentPoint = 0

   }
    var pointIndex = mutableListOf(startPoint)
    var currMapName = "Untitled Map"

    fun getCurrentPointFromIndex(currentPoint: Int): Point {
        var p= Point(0f,0f,0f,0)
        for (Point in PointsList.pointIndex){
            if(Point.pointNumber == currentPoint){
                p = Point
            }

        }
        return p

    }




}
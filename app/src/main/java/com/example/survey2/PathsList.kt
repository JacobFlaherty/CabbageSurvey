package com.example.survey2

import kotlin.math.abs

object PathsList {
    var pathIndex = mutableListOf<Path>()
    private fun countInList(list: MutableList<Point>,point: Point): Int {
        var counter = 0
        for (Point in list){
            if((abs(point.x - Point.x) < 0.00001)&&(abs(point.y - Point.y) < 0.00001)){
                counter++
            }
        }
        println("counter: "+counter)
        return counter

    }

    fun convertPathsToPoints(): MutableList<Point> {
        var pointsList = mutableListOf<Point>()
        var totalPoints = mutableListOf<Point>()


        for (Path in PathsList.pathIndex){
            totalPoints.add(Path.startPoint)
            totalPoints.add(Path.endPoint)
        }

        /*for(i in 0 until pathIndex.size){
            if(countInList(pointsList, pathIndex[i].startPoint) < 1){
                pointsList.add(pathIndex[i].startPoint)
            }


            if(countInList(pointsList, pathIndex[i].endPoint) < 1){
                pointsList.add(pathIndex[i].endPoint)
            }


        }

         */

        for(i in 0 until pathIndex.size){
            if((countInList(totalPoints, pathIndex[i].startPoint) == 1)){
                pointsList.add(pathIndex[i].startPoint)
                println("SP: "+pathIndex[i].startPoint)
            }
            else if((countInList(totalPoints, pathIndex[i].startPoint) > 2)){
                var newPoint = pathIndex[i].startPoint
                newPoint.isIntersection = true
                pointsList.add(pathIndex[i].startPoint)
                println(pathIndex[i].startPoint)
            }
            else if((countInList(totalPoints, pathIndex[i].startPoint) == 2)){
                var newPoint = pathIndex[i].startPoint

                pointsList.add(pathIndex[i].startPoint)
                println(pathIndex[i].startPoint)
            }
            if((countInList(totalPoints, pathIndex[i].endPoint) == 1)){
                pointsList.add(pathIndex[i].endPoint)
                println("EP: "+pathIndex[i].endPoint)
            }

        }
        println("List size is "+totalPoints.size)

        for(i in 0 until pointsList.size){
            if (countInList(pointsList, pointsList[i]) > 1){
                pointsList.removeAt(i)
            }
        }
        return pointsList



    }
}
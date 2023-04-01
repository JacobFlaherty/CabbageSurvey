package com.example.survey2


    fun getHighestPointIndex(): Int {
        var highestPoint = 0
        for(Point in PointsList.pointIndex){
            if(Point.pointNumber > highestPoint){
                highestPoint = Point.pointNumber
            }
        }
        return highestPoint
    }
    fun pointToString(point: Point): Array<String>{
        var str: Array<String> = arrayOf()

        str += point.x.toString()

        str += point.y.toString()

        str += point.z.toString()

        str += point.pointNumber.toString()

        str += point.pointName

        str += point.isIntersection.toString()

        for (Int in point.connectedPoints){

            str+=Int.toString()
        }
        println("POINTARRSAVED: ")
        for(String in str){
            println(String)
        }
        return str
    }

    fun pointsListToStringList(): List<Array<String>>{
        var list = mutableListOf<Array<String>>()
        for (Point in PointsList.pointIndex){
            list.add(pointToString(Point))
        }
        return list
    }

    fun stringListToPointsList(list: List<Array<String>>): List<Point>{
        var listPoints = mutableListOf<Point>()
        for (Array in list){
            var p = Point(0f,0f,0f,0)
            println("ARRAY 0: " + Array[0])
            p.x = Array[0].toFloat()
            p.y = Array[1].toFloat()
            p.z = Array[2].toFloat()
            p.pointNumber = Array[3].toInt()
            p.pointName = Array[4]
            p.isIntersection = Array[5].toBoolean()
            for(i in 0 until Array.size - 6){
                p.connectedPoints.add(Array[i+6].toInt())

            }
            listPoints.add(p)
        }
        return listPoints
    }



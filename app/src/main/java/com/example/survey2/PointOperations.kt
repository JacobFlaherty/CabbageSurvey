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
        str[0] = point.x.toString()

        str.plus(point.y.toString())

        str.plus(point.z.toString())

        str.plus(point.pointNumber.toString())

        str.plus(point.pointName)

        str.plus(point.isIntersection.toString())

        for (Int in point.connectedPoints){

            str.plus(Int.toString())
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
            p.x = Array[0].toFloat()
            p.y = Array[1].toFloat()
            p.z = Array[2].toFloat()
            p.pointNumber = Array[3].toInt()
            p.pointName = Array[4]
            for(i in 0 until Array.size - 5){
                p.connectedPoints.add(Array[i+4].toInt())

            }
            listPoints.add(p)
        }
        return listPoints
    }

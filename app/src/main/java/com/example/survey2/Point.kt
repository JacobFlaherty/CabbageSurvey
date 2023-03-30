package com.example.survey2

class Point(var x: Float, var y: Float, var z: Float, var pointNumber: Int) {
    var pointName = "unnamed"
    var isIntersection = false
    var connectedPoints = mutableListOf<Int>()

}
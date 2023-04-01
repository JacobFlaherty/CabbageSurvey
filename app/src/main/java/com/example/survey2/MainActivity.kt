package com.example.survey2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
import com.example.survey2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Math.cos
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

typealias LumaListener = (luma: Double) -> Unit





class MainActivity : AppCompatActivity(), SensorEventListener {





    lateinit var distanceInput: EditText


    var distance: Double = 0.0
    var heading = 0f
    var inclination = 0f
    var startPoint = Point(0f,0f,0f,0)
    var canMoveOn = true

    //var currentPoint = Point(0f,0f,0f)
    //val pointIndex = PointsList.pointIndex
    //val pathIndex = mutableListOf<Path>()

    //var surveyorPosition = arrayOf(0f,0f,0f)
    private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService


    fun resetPointText(){
        val txt = "Current Point: "+ PointsList.currentPoint
        val pointText: TextView = findViewById(R.id.pointTextView) as TextView
        pointText.text = txt
    }








    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) //possibly bad
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private var currentDegree = 0f

    private var mSensorManager: SensorManager? = null
    private fun getLargestConnectedPoint(point: Point): Int {
        var highestInt = 0
        for (Int in point.connectedPoints){
            if (Int>highestInt){
                highestInt=Int
            }
        }
        return highestInt
    }
    private fun getSmallestConnectedPoint(point: Point): Int {
        var lowestInt = 9999999
        for (Int in point.connectedPoints){
            if (Int<lowestInt){
                lowestInt=Int
            }
        }
        return lowestInt
    }
    fun cycleNextPoint(){
        if(getLargestConnectedPoint(PointsList.getCurrentPointFromIndex(PointsList.currentPoint)) > PointsList.currentPoint){
            PointsList.currentPoint = getLargestConnectedPoint(PointsList.getCurrentPointFromIndex(PointsList.currentPoint))
        }
    }
    fun cyclePrevPoint(){
        if(getSmallestConnectedPoint(PointsList.getCurrentPointFromIndex(PointsList.currentPoint)) < PointsList.currentPoint){
            PointsList.currentPoint = getSmallestConnectedPoint(PointsList.getCurrentPointFromIndex(PointsList.currentPoint))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initData()
        resetPointText()
        val nextButton = findViewById<ImageButton>(R.id.nextptButton)
        val prevButton = findViewById<ImageButton>(R.id.prevptButton)

        nextButton.setOnClickListener {
            cycleNextPoint()
            resetPointText()
        }

        prevButton.setOnClickListener {
            cyclePrevPoint()
            resetPointText()
        }



        var btnList = findViewById<Button>(R.id.listButton)
        btnList.setOnClickListener{
            val intent = Intent(this, DataListActivity::class.java)
            startActivity(intent)
        }
        //var toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)



        distanceInput = findViewById(R.id.distanceInput)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.createPathButton.setOnClickListener { createPoint() }
        viewBinding.setIntersectionButton.setOnClickListener { setIntersection() }
        viewBinding.distanceButton.setOnClickListener { distanceInput()}


        cameraExecutor = Executors.newSingleThreadExecutor()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.openItem -> {
                Toast.makeText(this, "Load Map",Toast.LENGTH_LONG).show()
                true
            }

            R.id.saveItem -> {
                Toast.makeText(this, "Save Map",Toast.LENGTH_LONG).show()
                true
            }

            R.id.saveAsItem -> {
                Toast.makeText(this, "Save Map As",Toast.LENGTH_LONG).show()
                true
            }
            R.id.settingsItem -> {
                Toast.makeText(this, "Settings",Toast.LENGTH_LONG).show()
                true
            }
            R.id.importExportItem -> {
                Toast.makeText(this, "Import/Export Data",Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }


    private fun relativePointToAbsolute(pointRelative: Point, relativePointOrigin: Point): Point {
        var mRelativePointOrigin = relativePointOrigin
        var mRelativePoint = pointRelative
        val absoluteX = mRelativePointOrigin.x + mRelativePoint.x
        val absoluteY = mRelativePointOrigin.y + mRelativePoint.y
        val absoluteZ = mRelativePointOrigin.z + mRelativePoint.z

        return Point(absoluteX, absoluteY, absoluteZ, 0)


    }


/*
    private fun createPath() {
        createPoint()
        val newPath = Path(PointsList.pointIndex[PointsList.currentPoint], PointsList.pointIndex[PointsList.pointIndex.size-1])
        PathsList.pathIndex.add(newPath)
        if(!PointsList.pointIndex[PointsList.currentPoint].isIntersection){
            PointsList.currentPoint = PointsList.pointIndex.size-1
        }
        val currentPointView = findViewById<TextView>(R.id.pointTextView)
        currentPointView.text = PointsList.pointIndex[PointsList.currentPoint].x.toString()




    }

 */





    private fun createPoint(){
        val newPoint = relativePointToAbsolute(calculatePointRelative(distance.toFloat(), heading,inclination),PointsList.pointIndex[PointsList.currentPoint])
        newPoint.pointNumber = getHighestPointIndex()+1
        newPoint.connectedPoints.add(PointsList.currentPoint)
        PointsList.pointIndex.add(newPoint)
        PointsList.pointIndex[PointsList.currentPoint].connectedPoints.add(newPoint.pointNumber)
        if(canMoveOn) {
            PointsList.currentPoint = PointsList.pointIndex.size - 1
            resetPointText()
        }


    }

    private fun deletePoint(){
        PointsList.pointIndex.removeAt(PointsList.pointIndex.size-1)
        resetPointText()
    }

    private fun calculatePointRelative(
        distance: Float,
        angle: Float,
        inclination: Float
    ): Point {
        var x = 0f
        var y = 0f
        var z = 0f
        var hD = getHorizontalDistance(distance,inclination)
        if (angle <= 90f) {
            x = hD * -sin(degreesToRadians(angle))

            y = hD * kotlin.math.cos(degreesToRadians(angle))


        } else if (angle > 90f && angle <= 180f) {
            x = hD * sin(degreesToRadians(180f - angle))

            y = hD * kotlin.math.cos(degreesToRadians(180f - angle)) * -1f

        } else if (angle > 180f && angle <= 270f) {
            x = hD * sin(degreesToRadians(angle - 180f))

            y = hD * kotlin.math.cos(degreesToRadians(angle - 180f)) * -1f

        } else if (angle > 270f && angle <= 360f) {
            x = hD * sin(degreesToRadians(360f - angle))

            y = hD * kotlin.math.cos(degreesToRadians(360f - angle))

        }

        z = -distance * sin(degreesToRadians(inclination)+(PI/2f).toFloat())

        //return arrayOf(x, y, z)
        return Point(x, y, z, 0)


    }
    private fun degreesToRadians(degrees: Float): Float {
        return (degrees * (PI / 180f)).toFloat()

    }

    private fun getHorizontalDistance(d: Float, inclination: Float): Float {
        return d * kotlin.math.cos(degreesToRadians(inclination)+(PI/2f).toFloat())
    }


    private fun setIntersection() {
        canMoveOn = false
        PointsList.getCurrentPointFromIndex(PointsList.currentPoint).isIntersection = true


    }

    private fun distanceInput(){
        distance = distanceInput.text.toString().toDouble()

        println(distance)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private fun initData(){
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?

    }

    override fun onResume() {
        super.onResume()
        @Suppress("DEPRECATION")
        mSensorManager?.registerListener(this,
        mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
        SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        val degree = (event?.values?.get(0)!!).roundToInt()
        val upDegree = (event?.values?.get(1)!!).roundToInt()


        val rotateAnimation = RotateAnimation(
            currentDegree,
            (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f

        )
        rotateAnimation.duration = 210
        rotateAnimation.fillAfter = true


        findViewById<ImageView>(R.id.imageView2).startAnimation(rotateAnimation)
        currentDegree = (-degree).toFloat()

        heading = currentDegree
        inclination = upDegree.toFloat()



    }






}
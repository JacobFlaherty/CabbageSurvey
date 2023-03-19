package com.example.survey2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DataListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)

        //val listView = findViewById<ListView>(R.id.itemList)
        var points = PointsList.pointIndex
        var xList = mutableListOf<Float>()
        var yList = mutableListOf<Float>()
        var zList = mutableListOf<Float>()
        for (Point in points) xList.add(Point.x)
        for (Point in points) yList.add(Point.y)
        for (Point in points) zList.add(Point.z)
        val xArray: Array<Float> = xList.toTypedArray()
        val yArray: Array<Float> = yList.toTypedArray()
        val zArray: Array<Float> = zList.toTypedArray()
        val pointArray: Array<Point> = points.toTypedArray()

        var btnCam = findViewById<Button>(R.id.camButton)
        btnCam.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        var mapNameInput = findViewById<EditText>(R.id.editTextMapName)
        var mapNameInputButton = findViewById<Button>(R.id.enterButton)
        mapNameInputButton.setOnClickListener{MapsList.currMapName = mapNameInput.text.toString()


        }
        val adapter = PointViewAdapter(PointsList.pointIndex)
        val rv = findViewById<View>(R.id.rvPoints) as RecyclerView

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)



        //val arrayAdapter: ArrayAdapter<Point> = ArrayAdapter(this, android.R.layout.simple_list_item_1,pointArray)


        /*listView.setOnItemClickListener{ adapterView, view, i, l ->
            Toast.makeText(this, "Point Selected", Toast.LENGTH_LONG).show()
        }

         */
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu);
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true;

    }
    private fun convertMapToStorable(map: com.example.survey2.Map): InternalStorageMap {
        var internalMap = InternalStorageMap(map.name)
        for (Path in map.paths){
            internalMap.startPointsX.add(Path.startPoint.x)
            internalMap.startPointsY.add(Path.startPoint.y)
            internalMap.startPointsZ.add(Path.startPoint.z)

            internalMap.endPointsX.add(Path.endPoint.x)
            internalMap.endPointsY.add(Path.endPoint.y)
            internalMap.endPointsZ.add(Path.endPoint.z)
        }


        return internalMap
    }
    private fun saveMapToInternalStorage(filename: String, map: InternalStorageMap){
        val mMapEncodeDecode = MapEncodeDecode()
        val path: File = applicationContext.filesDir

        try {
            val writer: FileOutputStream = FileOutputStream(File(path, filename+".cbg"))
            writer.write(mMapEncodeDecode.encode(map).toByteArray())
            writer.close()
        } catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when(item.itemId)
        return when(item.itemId){
            R.id.openItem -> {
                val intent = Intent(this, FileViewActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.saveItem -> {
                if(MapsList.currMapName == ""){
                    Toast.makeText(this, "Please enter map name",Toast.LENGTH_LONG).show()
                }
                else{
                    var mapToSave: com.example.survey2.Map = Map(MapsList.currMapName,PathsList.pathIndex)
                    saveMapToInternalStorage(MapsList.currMapName, convertMapToStorable(mapToSave))
                    Toast.makeText(this, "Saved",Toast.LENGTH_LONG).show()
                }
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


}
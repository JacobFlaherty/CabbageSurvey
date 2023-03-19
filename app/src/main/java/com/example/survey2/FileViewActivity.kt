package com.example.survey2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class FileViewActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_view)

        //val listView = findViewById<ListView>(R.id.fileList)

        //listView.setBackgroundColor(Color.parseColor("#171a1c"))

        //listView.adapter = CustomAdapter(this)

        MapsList.mapIndex = loadMapsToList()
        val adapter = FileViewAdapter(MapsList.mapIndex)
        val rv = findViewById<View>(R.id.rvFiles) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        var btnCam = findViewById<Button>(R.id.camButton2)
        btnCam.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //println("TestIndex: " + PointsList.pointIndex[0].x)
        }





        val mMapEncodeDecode = MapEncodeDecode()


        var accessibleMapsList = mutableListOf<com.example.survey2.Map>()
        var mapFileList: List<File> = listReadableFiles()
        //loadMapsToList()
        for(File in mapFileList){
            println(mMapEncodeDecode.decode(loadMapFromInternalStorage(File.name)).endPointsX)
        }






    }
    private fun loadMapsToList(): MutableList<com.example.survey2.Map> {
        val mMapEncodeDecode = MapEncodeDecode()


        var accessibleMapsList = mutableListOf<com.example.survey2.Map>()
            var mapFileList: List<File> = listReadableFiles()
        for(File in mapFileList){
            accessibleMapsList.add(
                convertStorableMapToMap(mMapEncodeDecode.decode(loadMapFromInternalStorage(File.name)))
                )
        }










        return accessibleMapsList




    }

    fun createByteArray(size: Long): ByteArray {
        val bufferSize = 1024 * 1024 // 1 MB buffer
        val buffer = ByteArray(bufferSize)
        var remaining = size
        val outputStream = ByteArrayOutputStream()
        while (remaining > 0) {
            val bytesToWrite = if (remaining > bufferSize) bufferSize else remaining.toInt()
            outputStream.write(buffer, 0, bytesToWrite)
            remaining -= bytesToWrite
        }
        return outputStream.toByteArray()
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu);
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true;

    }
    private fun deleteMapFromInternalStorage(filename: String): Boolean{
        return try{
            deleteFile(filename)

        }
        catch (e: java.lang.Exception){
            e.printStackTrace()
            false
        }
    }

    private fun convertStorableMapToMap(map: InternalStorageMap): com.example.survey2.Map {
        var localPathIndex = mutableListOf<Path>()
        if(map.startPointsX.size != null){
            for (i in 0 until map.startPointsX.size){
                localPathIndex.add(
                    Path(
                        Point(
                            map.startPointsX[i],map.startPointsY[i],map.startPointsZ[i]),

                        Point(
                            map.endPointsX[i],map.endPointsY[i],map.endPointsZ[i]),


                        )
                )

            }
        }



        return Map(map.name, localPathIndex)
    }
    private fun loadMapFromInternalStorage(fileName: String): String {
        val path: File = applicationContext.filesDir
        var readFrom: File = File(path, fileName)
        var content = createByteArray(readFrom.length() )

        try {
            val stream: FileInputStream = FileInputStream(readFrom)
            stream.read(content)
            return String(content)
        }
        catch (e: Exception){
            e.printStackTrace()
            return e.toString()
        }
    }
    private fun listReadableFiles(): List<File> {
        val files = filesDir.listFiles()
        if (files != null) {
            return files.filter { it.canRead() && it.isFile && it.name.endsWith(".cbg")}
        }
        else{
            return listOf()
        }

    }
    /*return withContext(Dispatchers.IO){
            val files = filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".cbg")}?.map {
                val bytes = it.readBytes()
                val map = ObjectSerializer.deserialize(bytes.decodeToString())
                InternalStorageMap(it.name, map as com.example.survey2.Map)

            }?: listOf()
        }
        */




}
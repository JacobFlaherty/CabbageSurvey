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
        var noFilesText:TextView = findViewById(R.id.noFilesTextView)
        var path: String? = intent.getStringExtra("path")
        var root = File(path)
        var filesAndFolders: Array<File> = root.listFiles()

        if (filesAndFolders == null || filesAndFolders.size == 0){
            noFilesText.visibility = View.VISIBLE
            return
        }
        noFilesText.visibility = View.INVISIBLE


        val adapter = FileViewAdapter(filesAndFolders)
        val rv = findViewById<View>(R.id.rvFiles) as RecyclerView

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        var btnCam = findViewById<Button>(R.id.camButton2)
        btnCam.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //println("TestIndex: " + PointsList.pointIndex[0].x)
        }













    }









    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu);
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true;

    }









}
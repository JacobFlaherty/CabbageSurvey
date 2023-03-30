package com.example.survey2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.survey2.databinding.ItemFileBinding
import java.io.File
import java.nio.file.Files
import com.example.survey2.CsvImportExport


class FileViewAdapter(
    var filesAndFolders: Array<File>
) : RecyclerView.Adapter<FileViewAdapter.FileViewHolder>() {

    inner class FileViewHolder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root){

    }
    lateinit var thisContext: Context
    val CsvOps = CsvImportExport()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        //return FileViewHolder(view)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFileBinding.inflate(layoutInflater,parent,false)
        thisContext = parent.context
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = filesAndFolders[position].name
            btnDelete.setOnClickListener {



            }
            btnLoad.setOnClickListener{
                PointsList.pointIndex = stringListToPointsList(CsvOps.readLineByLine(filesAndFolders[position].name)).toMutableList()








            }
        }
    }

    override fun getItemCount(): Int {
        return filesAndFolders.size
    }

}
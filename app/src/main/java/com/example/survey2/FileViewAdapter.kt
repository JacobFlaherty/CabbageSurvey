package com.example.survey2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.survey2.databinding.ItemFileBinding

class FileViewAdapter(
    var maps: List<com.example.survey2.Map>
) : RecyclerView.Adapter<FileViewAdapter.FileViewHolder>() {

    inner class FileViewHolder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root){

    }
    lateinit var thisContext: Context




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
            tvTitle.text = maps[position].name
            btnDelete.setOnClickListener {
                MapsList.deleteMap(maps[position],thisContext)

            }
            btnLoad.setOnClickListener{
                PathsList.pathIndex = maps[position].paths.toMutableList()
                PointsList.pointIndex = PathsList.convertPathsToPoints()
                println(PointsList.pointIndex)


            }
        }
    }

    override fun getItemCount(): Int {
        return maps.size
    }

}
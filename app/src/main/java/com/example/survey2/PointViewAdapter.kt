package com.example.survey2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.example.survey2.databinding.ItemPointBinding

class PointViewAdapter(
    var maps: List<Point>
) : RecyclerView.Adapter<PointViewAdapter.PointViewHolder>() {

    inner class PointViewHolder(val binding: ItemPointBinding) : RecyclerView.ViewHolder(binding.root){

    }
    lateinit var thisContext: Context




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        //return FileViewHolder(view)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPointBinding.inflate(layoutInflater,parent,false)
        thisContext = parent.context
        return PointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.binding.apply {
            val titleText = "Point #"+PointsList.pointIndex[position].pointNumber
            tvTitle.text = titleText
            textViewX.text = PointsList.pointIndex[position].x.toString()
            textViewY.text = PointsList.pointIndex[position].y.toString()
            textViewZ.text = PointsList.pointIndex[position].z.toString()
            if(PointsList.pointIndex[position].isIntersection == true){
                interSectionSwitch.isChecked = true
            }
            else{
                interSectionSwitch.isChecked = false
            }
            
            interSectionSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked)
                    PointsList.pointIndex[position].isIntersection = true
                else{
                    PointsList.pointIndex[position].isIntersection = false
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return maps.size
    }

}
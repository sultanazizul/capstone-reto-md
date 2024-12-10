package com.example.reto.ui.knowledge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reto.R

class TypeWasteAdapter(private val typeWasteList: List<TypeWaste>) :
    RecyclerView.Adapter<TypeWasteAdapter.TypeWasteViewHolder>() {

    class TypeWasteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeWasteImageView: ImageView = itemView.findViewById(R.id.ivTypeWaste)
        val typeWasteNameTv: TextView = itemView.findViewById(R.id.tvTypeWaste)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TypeWaste)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeWasteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge, parent, false)
        return TypeWasteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return typeWasteList.size
    }

    override fun onBindViewHolder(holder: TypeWasteViewHolder, position: Int) {
        val typeWaste = typeWasteList[position]
        holder.typeWasteImageView.setImageResource(typeWaste.typeWasteImage)
        holder.typeWasteNameTv.text = typeWaste.typeWasteName

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(typeWasteList[holder.adapterPosition])
        }
    }
}
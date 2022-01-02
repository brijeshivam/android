package com.oyelabs.marvel.universe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.net.URL

class RAdapter(private val listener: ItemClicked) : RecyclerView.Adapter<RAdapter.ViewHolder>() {
    private var charactersDataList = ArrayList<CharactersData>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        val viewHolder = ViewHolder(v)
        v.setOnClickListener {
            listener.onItemClick(charactersDataList[viewHolder.adapterPosition].link)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RAdapter.ViewHolder, position: Int) {
        val name = charactersDataList[position].name
        holder.textView.text = name
        Glide.with(holder.itemView.context).asBitmap()
            .load(charactersDataList[position].thumbnail)
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
                    .error(R.drawable.ic_baseline_image_not_supported_24)
            )
            .into(holder.imageView)


    }

    override fun getItemCount(): Int {
        return charactersDataList.size
    }

    fun updateData(updatedDataList: ArrayList<CharactersData>) {
        charactersDataList.addAll(updatedDataList)
        notifyDataSetChanged()
    }
    fun search(update:CharactersData){
        charactersDataList.add(0, update)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cardImage)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}

interface ItemClicked {
    fun onItemClick(link: String)
}



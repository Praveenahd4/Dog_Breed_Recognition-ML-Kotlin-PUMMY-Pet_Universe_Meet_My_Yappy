package com.pummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BreedAdapter(private val breeds: List<Breed>, private val itemClickListener: (Breed) -> Unit) : RecyclerView.Adapter<BreedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val breedNameTextView: TextView = itemView.findViewById(R.id.breedNameTextView)
        val breedLifeSpanTextView: TextView = itemView.findViewById(R.id.lifespan)
        val breedImageView: ImageView = itemView.findViewById(R.id.breedImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val breed = breeds[position]
        holder.breedNameTextView.text = breed.name
        holder.breedLifeSpanTextView.text = breed.lifespan
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(breed.imageResourceId, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.breedImageView.setImageResource(resourceId)
        } else {
            holder.breedImageView.setImageResource(R.drawable.add_screen_image_placeholder)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(breed)
        }
    }

    override fun getItemCount(): Int {
        return breeds.size
    }
}

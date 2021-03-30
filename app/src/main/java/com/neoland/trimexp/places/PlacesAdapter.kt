package com.neoland.trimexp.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.databinding.CardPlacesListBinding

class PlacesAdapter(private val callback: OnItemClicked) : RecyclerView.Adapter<PlacesAdapter.AdapterViewHolder>() {

    interface OnItemClicked {
        fun onItemClicked(place: String)
    }

    class AdapterViewHolder(val itemBinding: CardPlacesListBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private var places =  listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val itemBinding = CardPlacesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.itemBinding.textViewAdress.text = places[position]
        holder.itemBinding.root.setOnClickListener {
            callback.onItemClicked(places[position])
        }
    }

    override fun getItemCount(): Int {
        return places.count()
    }

    fun updateData(places : List<String>){
        this.places = places
        notifyDataSetChanged()
    }

}

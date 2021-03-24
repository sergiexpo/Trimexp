package com.neoland.trimexp.experiences.explorer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.entities.Experience

interface ExplorerExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}

class ExplorerExperiencesAdapter(val listener : ExplorerExperiencesAdapterInterface) : RecyclerView.Adapter<ExplorerExperiencesAdapter.ExperienceViewHolder>()  {

    var experiences = listOf<Experience>()

    class ExperienceViewHolder(val itemBinding: CardExperienceBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemBinding = CardExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var experience = experiences[position]

        holder.itemBinding.textViewTitleExp.text = experience.title
        holder.itemBinding.imageViewPhotoExp.setImageResource(experience.mainPhoto)
        holder.itemBinding.textViewLocation.text = experience.location
        holder.itemBinding.textViewDuration.text = experience.duration
        holder.itemBinding.textViewPrice.text = experience.price + " " + experience.divisa


        holder.itemBinding.root.setOnClickListener {
            listener.onItemClick(experiences[position])
        }

    }

    override fun getItemCount(): Int {
        return experiences.size
    }


    fun updateExperiences(experiences: List<Experience>){
        this.experiences = experiences
        notifyDataSetChanged()
    }



}
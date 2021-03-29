package com.neoland.trimexp.experiences.explorer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.entities.Experience
import java.text.DecimalFormat

interface ExplorerExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}

class ExplorerExperiencesAdapter(val listener : ExplorerExperiencesAdapterInterface) : RecyclerView.Adapter<ExplorerExperiencesAdapter.ExperienceViewHolder>()  {

    var experiences = listOf<Experience>()
    var lat = 0.0
    var long = 0.0

    class ExperienceViewHolder(val itemBinding: CardExperienceBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemBinding = CardExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var experience = experiences[position]
        var distance = experience.getDistanceFromUserCurrenLocation(lat, long)
        var formatDistance = DecimalFormat("#.00")

        holder.itemBinding.textViewTitleExp.text = experience.title
        experience.mainPhoto?.let{holder.itemBinding.imageViewPhotoExp.setImageResource(it)}
        holder.itemBinding.textViewLocation.setTextColor(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.imageViewLocation.setColorFilter(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.textViewLocation.text = formatDistance.format(distance).toString() + " m"
        holder.itemBinding.textViewDuration.text = experience.duration
        holder.itemBinding.textViewPrice.text = experience.price + " " + experience.currency


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

//Format Functions

    fun colorDistance(distance: Float) : String {
        if (distance < 1000F) {
            return "#419F00"
        } else{
           return  "#EC8800"
        }
    }

}
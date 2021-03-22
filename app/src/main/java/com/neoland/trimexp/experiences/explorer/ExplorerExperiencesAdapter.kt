package com.neoland.trimexp.experiences.explorer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.entities.Experience

interface ExplorerExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}

class ExplorerExperiencesAdapter(val listener : ExplorerExperiencesAdapterInterface) : RecyclerView.Adapter<ExplorerExperiencesAdapter.ExperienceViewHolder>()  {

    // var experiences = listOf<Experience>()
    var experiences = listOf(Experience("Human Towers", "Barcelona", "5 horas"),
            Experience("Harry potter tour", "London", "3 horas"),
            Experience("Torre eiffel", "Paris", "2 horas"),
            Experience("Visita cerdos", "Cuenca", "1 horas"),
            Experience("Musica en vivo", "Viena", "3 horas"),
            Experience("Museo prado guía", "Madrid", "4 horas"),
            Experience("tour fariña", "Pontevedra", "8 horas")
    )

    class ExperienceViewHolder(val itemBinding: CardExperienceBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemBinding = CardExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        holder.itemBinding.textViewTitleExp.text = experiences[position].title
        holder.itemBinding.textViewLocation.text = experiences[position].location
        holder.itemBinding.textViewDuration.text = experiences[position].duration


        holder.itemBinding.root.setOnClickListener {
            listener.onItemClick(experiences[position])
        }

    }

    override fun getItemCount(): Int {
        return experiences.size
    }


}
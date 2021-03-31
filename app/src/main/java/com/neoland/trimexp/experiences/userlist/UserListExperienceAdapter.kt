package com.neoland.trimexp.experiences.userlist

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.databinding.CardExperienceUserListBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapter
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapterInterface
import java.text.DecimalFormat


interface UserListExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}


class UserListExperienceAdapter(val listener : UserListExperiencesAdapterInterface) : RecyclerView.Adapter<UserListExperienceAdapter.ExperienceViewHolder>()  {

    var experiences = listOf<Experience>()
    var lat = 0.0
    var long = 0.0

    class ExperienceViewHolder(val itemBinding: CardExperienceUserListBinding) : RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListExperienceAdapter.ExperienceViewHolder {
        val itemBinding = CardExperienceUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListExperienceAdapter.ExperienceViewHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var experience = experiences[position]
        var distance = experience.getDistanceFromUserCurrenLocation(lat, long)
        var formatDistance = DecimalFormat("#.00")

        holder.itemBinding.textViewTitleExpUserOwner.text = experience.title
        experience.mainPhoto?.let{holder.itemBinding.imageViewPhotoExpUserOwner.setImageResource(it)}
        experience.photoExperience?.let{holder.itemBinding.imageViewPhotoExpUserOwner.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
        holder.itemBinding.textViewLocationUserOwner.setTextColor(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.imageViewLocationUserOwner.setColorFilter(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.textViewLocationUserOwner.text = formatDistance.format(distance).toString() + " m"
        holder.itemBinding.textViewDurationUserOwner.text = experience.duration
        holder.itemBinding.textViewDateUserOwner.text = experience.dateFrom.toString() // Formatear correctamente
        holder.itemBinding.textViewPriceUserOwner.text = experience.price + " " + experience.currency


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

// MARK - Format Functions

    fun colorDistance(distance: Float) : String {
        if (distance < 1000F) {
            return "#419F00"
        } else{
            return  "#EC8800"
        }
    }




}
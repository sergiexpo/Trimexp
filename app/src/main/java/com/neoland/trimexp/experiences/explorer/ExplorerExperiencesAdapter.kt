package com.neoland.trimexp.experiences.explorer

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.entities.Experience
import java.text.DecimalFormat
import kotlin.math.absoluteValue

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


        holder.itemBinding.textViewTitleExp.text = experience.title
        experience.mainPhoto?.let{holder.itemBinding.imageViewPhotoExp.setImageResource(it)}
        experience.photoExperience?.let{holder.itemBinding.imageViewPhotoExp.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
        holder.itemBinding.textViewLocation.setTextColor(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.imageViewLocation.setColorFilter(Color.parseColor(colorDistance(distance)))
        holder.itemBinding.textViewLocation.text = distanceInKm(distance) + " ${unitDistance(distance)}"
        holder.itemBinding.textViewDuration.text = formatDuration(experience.duration)
        holder.itemBinding.ratingBarCardExperience.rating = experience.ratingValoration
        holder.itemBinding.textViewPrice.setTextColor(Color.parseColor(colorPrice(experience.price)))
        // holder.itemBinding.textViewPrice.text = experience.price.toString() + " " + experience.currency
        holder.itemBinding.textViewPrice.text = formatIsFree(experience.price,experience.currency)

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

    // MARK - Format Functions Distance

    fun colorDistance(distance: Float) : String {
        if (distance < 3000F) {
            return "#419F00"
        } else{
            return  "#EC8800"
        }
    }

    fun distanceInKm(distance: Float) : String {

        var formatDistanceM = DecimalFormat("#")
        var formatDistanceKm = DecimalFormat("#.00")

        if (distance < 1000F) {
            return formatDistanceM.format(distance).toString()
        } else {
            return formatDistanceKm.format(distance / 1000).toString()
        }
    }

    fun unitDistance(distance: Float) : String {
        if (distance < 1000F) {
            return "m"
        } else{
            return  "Km"
        }
    }

    // MARK - Format Functions Duration

    fun formatDuration(duration: Float) : String{

        var formatDistanceM = DecimalFormat("#")

        if (duration.absoluteValue % 1.0 >= 0.005) {
            return duration.toString() + " hours"
        } else {
            return formatDistanceM.format(duration).toString() + " hours"
        }
    }

    // MARK - Format Functions Price

    fun colorPrice(price: Float) : String  {
        if (price == 0F) {
            return "#688db9"
        } else {
            return "#ff0000"
        }
    }

    fun formatIsFree(price: Float, currency: String) : String{

        var formatDistanceM = DecimalFormat("#")

        if (price == 0F) {
            return "Free"
        } else {
            if (price.absoluteValue % 1.0 >= 0.005) {
                return price.toString() + "$currency"
            } else {
                return formatDistanceM.format(price).toString() + "$currency"
            }
        }
    }


}
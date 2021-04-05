package com.neoland.trimexp.experiences.userlist

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.databinding.CardExperienceUserListBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapter
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapterInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


interface UserListExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}


class UserListExperienceAdapter(private val listener : UserListExperiencesAdapterInterface, private val userId: Int, context: Context) : RecyclerView.Adapter<UserListExperienceAdapter.ExperienceViewHolder>()  {

    var experiences = listOf<Experience>()
    var lat = 0.0
    var long = 0.0
    private var context = context

    class ExperienceViewHolder(val itemBinding: CardExperienceUserListBinding) : RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListExperienceAdapter.ExperienceViewHolder {
        val itemBinding = CardExperienceUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListExperienceAdapter.ExperienceViewHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var experience = experiences[position]

        holder.itemBinding.textViewTitleExpUserOwner.text = experience.title
        experience.mainPhoto?.let{holder.itemBinding.imageViewPhotoExpUserOwner.setImageResource(it)}
        experience.photoExperience?.let{holder.itemBinding.imageViewPhotoExpUserOwner.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}

        holder.itemBinding.textViewLocationUserOwner.text = getCity(experience.latitud, experience.longitud)
        holder.itemBinding.textViewDurationUserOwner.text = formatDuration(experience.duration)
        holder.itemBinding.textViewDateUserOwner.text = formatDate(Date(experience.dateFrom))

        holder.itemBinding.textViewPriceUserOwner.text = formatIsFree(experience.price,experience.currency)

        holder.itemBinding.textViewHourUserOwner.text = experience.startHour

        holder.itemBinding.linearLayoutCard.setBackgroundColor(Color.parseColor(colorTypeUser(experience)))
        holder.itemBinding.textViewUserType.text = textTypeUser(experience)

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

    private fun formatDate(date: Date): String {
        var simpleDateFormat = SimpleDateFormat("EEE dd MMM yyyy")
        return simpleDateFormat.format(date.time)

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

// MARK - Format Functions Location

    private fun getCity(lat: Double, lng: Double): String {

        val geocoder = Geocoder(context)

        val list = geocoder.getFromLocation(lat, lng, 1)
        if (list.isEmpty()){
            return "Address not found"
        } else {
            return list[0].locality + ", " + list[0].countryCode
        }
    }

// MARK - Format Functions Type User


    fun colorTypeUser(experience: Experience): String {
        if (experience.fkUserIdOwner == userId) {
            return  "#688db9"
        } else if (experience.fkUserIdRequester == userId) {
            return  "#EC8800"
        } else {
            return "#FFFFFF"
        }
    }

    fun textTypeUser(experience: Experience): String {
        if (experience.fkUserIdOwner == userId) {
            return  "Host"
        } else if (experience.fkUserIdRequester == userId) {
            return  "Guest"
        } else {
            return "Error"
        }
    }

    // MARK - Format Functions Price

    fun colorPrice(price: Float) : String  {
        if (price == 0F) {
            return "#419F00"
        } else {
            return "#688db9"
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
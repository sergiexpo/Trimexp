package com.neoland.trimexp.users.favourites

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.databinding.CardExperienceUserListBinding
import com.neoland.trimexp.databinding.CardUsersFavouritesBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import com.neoland.trimexp.experiences.manage.ManageExperienceAdapter
import com.neoland.trimexp.experiences.manage.ManageExperienceAdapterInterface
import java.text.DecimalFormat


interface FavouriteUsersListAdapterInterface{
    fun onItemClick(user: User)
}

class FavouriteUsersListAdapter(private val listener : FavouriteUsersListAdapterInterface, private val userId: Int) : RecyclerView.Adapter<FavouriteUsersListAdapter.ExperienceViewHolder>()  {

    var users = listOf<User>()



    class ExperienceViewHolder(val itemBinding: CardUsersFavouritesBinding) : RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemBinding = CardUsersFavouritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemBinding)
    }


    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var user = users[position]

        user.photoUser?.let{holder.itemBinding.imageViewPhotoUserFav.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
        user.mainPhoto?.let{holder.itemBinding.imageViewPhotoUserFav.setImageResource(it)}
        holder.itemBinding.textViewNameUserFav.text = user.name
        holder.itemBinding.textViewLocationUserFav.text = user.residentLocation
        holder.itemBinding.ratingBarUser.rating = user.ratingValoration

        holder.itemBinding.root.setOnClickListener {
            listener.onItemClick(users[position])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


    fun updateFavouriteUsers(users: List<User>){
        this.users = users
        notifyDataSetChanged()
    }


}
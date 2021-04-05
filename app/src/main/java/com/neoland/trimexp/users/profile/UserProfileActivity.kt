package com.neoland.trimexp.users.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.databinding.ActivityDetailExperienceBinding
import com.neoland.trimexp.databinding.ActivityProfileUserBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import com.neoland.trimexp.experiences.detail.ExperienceDetailViewModel
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    companion object {
        const val TAG21 = "UserFromList ID"
    }

    private lateinit var binding : ActivityProfileUserBinding
    private lateinit var model: UserProfileActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(UserProfileActivityViewModel::class.java)

        binding.imageViewIconBack.setOnClickListener {
            goBack()
        }


        lifecycleScope.launch(Dispatchers.Main) {

            val user =  model.getUser(intent.getIntExtra(TAG21, 0))
            user.mainPhoto?.let{ binding.profilePhoto.setImageResource(it)}
            user.photoUser?.let{binding.profilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
            binding.profileName.text = user.name
            binding.profileEmail.text = user.email
            binding.profileLocation.text = user.residentLocation
            binding.profileDescription.text = user.description
            binding.ratingBarUser.rating = user.ratingValoration

        }



    }



    private fun goBack(){
        onBackPressed()
    }

}
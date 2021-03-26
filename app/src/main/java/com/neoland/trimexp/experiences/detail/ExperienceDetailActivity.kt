package com.neoland.trimexp.experiences.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.databinding.ActivityDetailExperienceBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG1
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG10
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG11
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG12
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG2
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG3
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG4
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG6
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG9
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ExperienceDetailActivity: AppCompatActivity() {

    private lateinit var binding : ActivityDetailExperienceBinding
    private lateinit var model: ExperienceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)

        binding.imageViewIconBack.setOnClickListener {
            goBackExplorerActivity()
        }


        binding.imageViewMainPhoto.setImageResource(intent.getIntExtra(TAG1, 0))
        binding.textViewTitleDetailExp.text = intent.getStringExtra(TAG2)
        binding.textViewDescriptionDetailExp.text = intent.getStringExtra(TAG3)
        binding.textViewDateDetailExp.text = intent.getStringExtra(TAG4)
        binding.textViewDurationDetailExp.text = intent.getStringExtra(TAG6)
        binding.textViewPriceDetailExp .text = intent.getStringExtra(TAG9) + " " + intent.getStringExtra(TAG10)
        binding.textViewAdressDetailExp .text = intent.getStringExtra(TAG11)


        lifecycleScope.launch(Dispatchers.Main) {
          val user =  model.getUser(intent.getIntExtra(TAG12, 0))
            binding.textViewUserName.text = user.name
            binding.imageViewPhotoUser.setImageResource(user.mainPhoto)
        }

    }


    private fun goBackExplorerActivity(){
        onBackPressed()
    }

}
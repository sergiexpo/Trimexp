package com.neoland.trimexp.experiences.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neoland.trimexp.databinding.ActivityDetailExperienceBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG1
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG10
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG11
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG12
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG2
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG3
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG6
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG9


class ExperienceDetailActivity: AppCompatActivity() {

    private lateinit var binding : ActivityDetailExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewMainPhoto.setImageResource(intent.getIntExtra(TAG1, 0))
        binding.textViewTitleDetailExp.text = intent.getStringExtra(TAG2)
        binding.textViewDescriptionDetailExp.text = intent.getStringExtra(TAG3)
        binding.textViewDurationDetailExp.text = intent.getStringExtra(TAG6)
        binding.textViewPriceDetailExp .text = intent.getStringExtra(TAG9) + " " + intent.getStringExtra(TAG10)
        binding.textViewAdressDetailExp .text = intent.getStringExtra(TAG11)
        binding.textViewUserName.text = intent.getStringExtra(TAG12)



    }
}
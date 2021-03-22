package com.neoland.trimexp.experiences.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neoland.trimexp.databinding.ActivityDetailExperienceBinding


class ExperienceDetailActivity: AppCompatActivity() {

    private lateinit var binding : ActivityDetailExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
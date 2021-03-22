package com.neoland.trimexp.home

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neoland.trimexp.databinding.ActivityHomeBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity

class HomeActivity: AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var model: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())

        binding.imageViewIconMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.buttonExplore.setOnClickListener {
            startExplorerExperienceActivity()
        }


    }


    private fun startExplorerExperienceActivity(){
        val intent = Intent(this, ExplorerExperiencesActivity::class.java)
        startActivity(intent)
    }

}
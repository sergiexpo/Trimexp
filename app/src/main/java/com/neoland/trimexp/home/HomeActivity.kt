package com.neoland.trimexp.home

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neoland.trimexp.databinding.ActivityLoginBinding

class HomeActivity: AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var model: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())

        binding.imageViewIconMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }



    }
}
package com.neoland.trimexp.users.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neoland.trimexp.databinding.ActivityRegisterUserBinding

class RegisterUserActivity: AppCompatActivity() {

    lateinit var binding: ActivityRegisterUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageViewIconBack.setOnClickListener {
            goBackRegisterActivity()
        }

    }


    private fun goBackRegisterActivity(){
        onBackPressed()
    }


}
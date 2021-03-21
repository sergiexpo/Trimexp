package com.neoland.trimexp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neoland.trimexp.databinding.ActivityLoginBinding
import com.neoland.trimexp.databinding.ActivitySplashBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var model: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())


    }
}
package com.neoland.trimexp.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.databinding.ActivitySplashBinding
import com.neoland.trimexp.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity: AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var model: SplashActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(SplashActivityViewModel::class.java)

       binding.imageViewSplashAct.setImageResource(model.getSplashActivityImage())

       tiempoCarga()
    }




 /* FUNCIÓN TEMPORAL */

   private fun tiempoCarga(){

       lifecycleScope.launch {
           delay(2000)
           val intent = Intent(this@SplashActivity, HomeActivity::class.java)
           startActivity(intent)
           finish()
       }

   }

 /*FUNCIÓN TEMPORAL*/

}
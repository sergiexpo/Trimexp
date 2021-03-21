package com.neoland.trimexp.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.R

class SplashActivityViewModel (application: Application) : AndroidViewModel(application)  {


    fun getSplashActivityImage(): Int {
       return R.mipmap.splashactivityimage
    }

}
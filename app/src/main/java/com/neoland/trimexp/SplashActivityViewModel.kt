package com.neoland.trimexp

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SplashActivityViewModel (application: Application) : AndroidViewModel(application)  {


    fun getSplashActivityImage(): Int {
       return R.mipmap.splashactivityimage
    }

}
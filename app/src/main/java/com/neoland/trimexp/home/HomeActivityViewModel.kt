package com.neoland.trimexp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.R

class HomeActivityViewModel (application: Application) : AndroidViewModel(application)  {


    fun getLoginActivityImage(): Int {
        return R.mipmap.splashactivityimage
    }

}
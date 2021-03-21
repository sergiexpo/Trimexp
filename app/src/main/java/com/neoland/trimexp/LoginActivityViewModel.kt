package com.neoland.trimexp

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LoginActivityViewModel (application: Application) : AndroidViewModel(application)  {


    fun getLoginActivityImage(): Int {
        return R.mipmap.splashactivityimage
    }

}
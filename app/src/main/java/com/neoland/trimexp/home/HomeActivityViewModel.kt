package com.neoland.trimexp.home

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.DDBB.App
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.R
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class HomeActivityViewModel (application: Application) : AndroidViewModel(application)  {


    fun loadPreferences(tag: String) : String? {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getString(tag, "")
    }


    fun savePreferences(tag: String, string: String) {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(tag, string)
            commit()
        }
    }



    fun getLoginActivityImage(): Int {
        return R.mipmap.splashactivityimage
    }

    suspend fun existsUser(email: String, password: String) : Boolean  {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().isUserInDB(email, password)
        }
    }

    suspend fun getUser(email: String): User{
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUserbyEmail(email)
        }
    }

}
package com.neoland.trimexp.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.neoland.trimexp.ddbb.App
import com.neoland.trimexp.ddbb.Db
import com.neoland.trimexp.R
import com.neoland.trimexp.ddbb.DbStatus
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeActivityViewModel (application: Application) : AndroidViewModel(application)  {

    val status : LiveData<List<DbStatus>> = liveData { emitSource(Db.getDatabase(application).dbStatusDao().getAllLive()) }

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
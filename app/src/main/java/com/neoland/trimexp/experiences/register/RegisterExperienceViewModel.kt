package com.neoland.trimexp.experiences.register

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.ddbb.App
import com.neoland.trimexp.ddbb.Db
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class RegisterExperienceViewModel (application: Application) : AndroidViewModel(application)  {

    suspend fun insertExperience(title: String, description: String, adress: String, latitud: Double, longitud: Double, duration: Float, price: Float, currency : String, dateFrom: Long, dateTo: Long, owner: Int, bitmap: Bitmap?) {
        withContext(Dispatchers.IO) {
            val experience = Experience(title, null, description, null, adress, latitud, longitud, duration,
                                        true, "host", price, currency, null, null, dateFrom, dateTo, "10:00h",false, owner)
            bitmap?.let {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                experience.photoExperience = stream.toByteArray()
            }
            Db.getDatabase(getApplication()).experienceDao().insert(experience)
        }
    }

    fun loadPreferences(tag: String) : String? {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getString(tag, "")
    }

    suspend fun getUser(email: String): User {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUserbyEmail(email)
        }
    }






}



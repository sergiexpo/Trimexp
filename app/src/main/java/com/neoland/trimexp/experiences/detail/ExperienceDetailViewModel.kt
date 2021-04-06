package com.neoland.trimexp.experiences.detail

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.ddbb.App
import com.neoland.trimexp.ddbb.Db
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExperienceDetailViewModel (application: Application) : AndroidViewModel(application) {



    suspend fun getUser(id: Int): User{
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUser(id)
        }
    }

    suspend fun getUser(email: String): User{
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUserbyEmail(email)
        }
    }

    suspend fun getExperience(id: Int): Experience {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).experienceDao().getExperience(id)
        }
    }

    suspend fun experienceIsReserved(experience: Experience, userID: Int) {
        withContext(Dispatchers.IO) {
            experience.isReserved = true
            experience.fkUserIdRequester = userID
            Db.getDatabase(getApplication()).experienceDao().update(experience)
        }
    }


    fun loadPreferences(tag: String) : String? {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getString(tag, "")
    }


}
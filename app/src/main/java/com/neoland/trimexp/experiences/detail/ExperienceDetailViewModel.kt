package com.neoland.trimexp.experiences.detail

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExperienceDetailViewModel (application: Application) : AndroidViewModel(application) {



    suspend fun getUser(id: Int): User{
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUser(id)
        }
    }

    suspend fun getExperience(id: Int): Experience {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).experienceDao().getExperience(id)
        }
    }




}
package com.neoland.trimexp.experiences.manage

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.ddbb.App
import com.neoland.trimexp.ddbb.Db
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ManageExperienceFragmentViewModel (application: Application) : AndroidViewModel(application) {

    val experiences : MutableLiveData<List<Experience>> = MutableLiveData()

    fun getExperiences(managedFunction: (experiences: List<Experience>) -> List<Experience>){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = managedFunction(list)
            }
        }

    }


    fun getExperiencesOpen(userId : Int) {
        getExperiences { unFilteredList ->
            unFilteredList.filter { experience ->
                experience.dateFrom >= Calendar.getInstance().timeInMillis && (experience.fkUserIdOwner == userId || experience.fkUserIdRequester == userId)
            }.sortedBy {
                it.dateFrom
            }
        }
    }

    fun searchExperiencesInManagerList(text : String){

        experiences.value = experiences.value?.filter { it.title.contains(text)}

    }

    fun deleteExperience(experience: Experience, userId: Int){

        viewModelScope.launch(Dispatchers.IO) {
            Db.getDatabase(getApplication()).experienceDao().delete(experience)

            getExperiencesOpen(userId)
          /*  val allExperiences = Db.getDatabase(getApplication()).experienceDao().getAll()

            withContext(Dispatchers.Main) {
                experiences.value = allExperiences
            } */
        }

    }

    suspend fun getUser(email: String): User {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUserbyEmail(email)
        }
    }


    fun loadPreferences(tag: String) : String? {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getString(tag, "")
    }

  suspend fun unbookExperience(experience: Experience, userId: Int) {
        withContext(Dispatchers.IO) {
            experience.isReserved = false
            experience.fkUserIdRequester = null
            Db.getDatabase(getApplication()).experienceDao().update(experience)
            getExperiencesOpen(userId)
        }
    }


}
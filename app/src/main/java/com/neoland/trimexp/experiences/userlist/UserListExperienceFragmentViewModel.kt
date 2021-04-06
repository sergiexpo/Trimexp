package com.neoland.trimexp.experiences.userlist

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

class UserListExperienceFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val experiences : MutableLiveData<List<Experience>> = MutableLiveData()

    fun getExperiences(managedFunction: (experiences: List<Experience>) -> List<Experience>){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = managedFunction(list)
            }
        }

    }


    suspend fun getUser(email: String): User {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUserbyEmail(email)
        }
    }

    fun filterExperiencesOpen(userId : Int) {
        getExperiences { unFilteredList ->
            unFilteredList.filter { experience ->
                experience.dateFrom >= Calendar.getInstance().timeInMillis && (experience.fkUserIdOwner == userId || experience.fkUserIdRequester == userId)
            }.sortedBy {
                it.dateFrom
            }
        }
    }

    fun filterExperiencesHistorical(userId : Int) {
        getExperiences { unFilteredList ->
            unFilteredList.filter { experience ->
                experience.dateFrom < Calendar.getInstance().timeInMillis && (experience.fkUserIdOwner == userId || experience.fkUserIdRequester == userId)
            }.sortedBy {
                it.dateFrom
            }
        }
    }


    fun loadPreferences(tag: String) : String? {
        val sharedPreferences = getApplication<App>().getSharedPreferences("Preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getString(tag, "")
    }

}
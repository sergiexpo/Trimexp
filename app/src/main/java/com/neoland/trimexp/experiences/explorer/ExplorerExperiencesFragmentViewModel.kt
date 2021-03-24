package com.neoland.trimexp.experiences.explorer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.R
import com.neoland.trimexp.entities.Experience
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExplorerExperiencesFragmentViewModel (application: Application) : AndroidViewModel(application) {

    val experiences : MutableLiveData<List<Experience>> = MutableLiveData()

    fun getAllExperiences(){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = list
            }
        }

    }

    fun sortExperiencesByAscendingName() {

        sort { unSortedList ->
            unSortedList.sortedBy { experience ->
                experience.title }
        }
    }


    fun sortExperiencesByDescendingName() {

        sort { unSortedList ->
            unSortedList.sortedBy { experience ->
                experience.title }.reversed()
        }
    }


    fun sort(sortedFunction: (experiences : List<Experience>) -> List<Experience>){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = sortedFunction(list)
            }
        }

    }



}
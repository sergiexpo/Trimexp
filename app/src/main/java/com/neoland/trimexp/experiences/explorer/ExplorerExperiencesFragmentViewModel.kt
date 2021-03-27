package com.neoland.trimexp.experiences.explorer

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.entities.Experience
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExplorerExperiencesFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val experiences : MutableLiveData<List<Experience>> = MutableLiveData()

    fun getAllExperiences(){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = list
            }
        }

    }

    fun manage(managedFunction: (experiences: List<Experience>) -> List<Experience>){

        viewModelScope.launch(Dispatchers.IO) {
            val list = Db.getDatabase(getApplication()).experienceDao().getAll()
            withContext(Dispatchers.Main){
                experiences.value = managedFunction(list)
            }
        }

    }





    fun getExplorerExperiencesList(lat: Double, long: Double, date: Long) {

        var currentLocation = Location("Current")
        currentLocation.latitude = lat
        currentLocation.longitude = long

        var experienceLocation = Location("Experience")

        manage { unFilteredList ->
            unFilteredList.filter { experience ->
                experience.dateFrom > date
            }.sortedBy {
                experienceLocation.latitude = it.latitud
                experienceLocation.longitude = it.longitud
                currentLocation.distanceTo(experienceLocation)
            }
        }
    }


    //////// SORT FUNCTIONS ////////


    fun sortExperiencesByAscendingName() {

      /*  manage { unSortedList ->
            unSortedList.sortedBy { experience ->
                experience.title }
        } */

       experiences.value = experiences.value?.sortedBy { experience ->
            experience.title }
    }


    fun sortExperiencesByDescendingName() {

    /*    manage { unSortedList ->
            unSortedList.sortedBy { experience ->
                experience.title }.reversed()
        } */
        experiences.value = experiences.value?.sortedBy { experience ->
            experience.title }?.reversed()
    }




    //////// Filter FUNCTIONS ////////

    fun filterExperiencesFromDate(date: Long) {
        manage { unFilteredList ->
            unFilteredList.filter { experience ->
             /*   val dateUnique = experience.dateFrom
                if (dateUnique != null) {
                    dateUnique > date
                } else {
                    false
                } */
                experience.dateFrom > date
            }
        }
    }


    fun filterExperiencesAll(date: Long){
        filterExperiencesFromDate(date)
    }

    fun filterExperiencesFree() {
      /*  manage { unFilteredList ->
            unFilteredList.filter { experience ->
                experience.price == "Free"
            }
        }*/
        experiences.value = experiences.value?.filter { experience ->
            experience.price == "Free"}
    }


    fun filterExperiencesHost() {

        experiences.value = experiences.value?.filter { experience ->
            experience.typeExperience == "Host"}
    }

    fun filterExperiencesGuest() {

        experiences.value = experiences.value?.filter { experience ->
            experience.typeExperience == "Guest"}
    }



}
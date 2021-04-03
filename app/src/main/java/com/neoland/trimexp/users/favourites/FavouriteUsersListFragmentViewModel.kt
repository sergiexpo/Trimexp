package com.neoland.trimexp.users.favourites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neoland.trimexp.DDBB.App
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import com.neoland.trimexp.entities.UsersFavourites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteUsersListFragmentViewModel (application: Application) : AndroidViewModel(application)  {

  //  val usersFavouritesRelations : MutableLiveData<List<UsersFavourites>> = MutableLiveData()
    val usersFavorites :  MutableLiveData<List<User>> = MutableLiveData()


    fun getUsersFavourites(userID: Int){

        viewModelScope.launch(Dispatchers.IO) {
            val users = mutableListOf<User>()
            val list = Db.getDatabase(getApplication()).UsersFavouritesDAO().getUserFavourites(userID)

            list.forEach {
                users.add(Db.getDatabase(getApplication()).userDAO().getUser(it.fkuserFavouriteID))
            }

            withContext(Dispatchers.Main){
                usersFavorites.value = users
            }
        }

    }


    fun insertFavorite(usersFavorite: UsersFavourites){
        viewModelScope.launch(Dispatchers.IO) {

            Db.getDatabase(getApplication()).UsersFavouritesDAO().insert(usersFavorite)

        }
    }


    fun deleteFavorite(userId: Int, userFavoriteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

         var favorite = Db.getDatabase(getApplication()).UsersFavouritesDAO().getUserFavourite(userId, userFavoriteId)
            Db.getDatabase(getApplication()).UsersFavouritesDAO().delete(favorite)
            getUsersFavourites(userId)
        }
    }

    fun filterUsersInFavoriteList(text : String){

        usersFavorites.value = usersFavorites.value?.filter { it.name.contains(text) || it.residentLocation.contains(text) }

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



}
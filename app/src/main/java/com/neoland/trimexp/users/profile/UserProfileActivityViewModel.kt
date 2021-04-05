package com.neoland.trimexp.users.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProfileActivityViewModel (application: Application) : AndroidViewModel(application)  {

    suspend fun getUser(id: Int): User {
        return  withContext(Dispatchers.IO){
            Db.getDatabase(getApplication()).userDAO().getUser(id)
        }
    }


}
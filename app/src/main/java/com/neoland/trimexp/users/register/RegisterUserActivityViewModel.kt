package com.neoland.trimexp.users.register

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.neoland.trimexp.DDBB.Db
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class RegisterUserActivityViewModel (application: Application) : AndroidViewModel(application)  {

    suspend fun insertUser(name: String, description: String, email: String, password: String, bitmap: Bitmap?) {
        withContext(Dispatchers.IO) {
            val user = User(name, description, email, password)
            bitmap?.let {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                user.photoUser = stream.toByteArray()
            }
            Db.getDatabase(getApplication()).userDAO().insert(user)
        }
    }

}
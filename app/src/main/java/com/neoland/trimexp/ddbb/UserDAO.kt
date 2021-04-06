package com.neoland.trimexp.ddbb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neoland.trimexp.entities.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun getAllLive(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE User.userId = :id")
    fun getUser(id: Int): User

    @Query("SELECT * FROM User WHERE User.email = :email")
    fun getUserbyEmail(email: String,): User

    @Query("SELECT * FROM User WHERE User.email = :email AND User.password = :password")
    fun isUserInDB(email: String, password: String): Boolean

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Insert
    fun insertAll(user: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMutable(user: MutableList<User>?)

    @Delete
    fun delete(user: User)
}
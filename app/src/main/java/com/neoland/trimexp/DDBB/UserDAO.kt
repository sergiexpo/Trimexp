package com.neoland.trimexp.DDBB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun getAllLive(): LiveData<List<User>>

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
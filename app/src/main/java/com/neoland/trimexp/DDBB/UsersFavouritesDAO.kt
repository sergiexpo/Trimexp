package com.neoland.trimexp.DDBB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neoland.trimexp.entities.User
import com.neoland.trimexp.entities.UsersFavourites

@Dao
interface UsersFavouritesDAO {

        @Query("SELECT * FROM UsersFavourites")
        fun getAll(): List<UsersFavourites>

        @Query("SELECT * FROM UsersFavourites")
        fun getAllLive(): LiveData<List<UsersFavourites>>

        @Query("SELECT * FROM UsersFavourites WHERE UsersFavourites.fkuserID = :id")
        fun getUserFavourites(id: Int): List<UsersFavourites>

        @Insert
        fun insert(userFavourites: UsersFavourites)

        @Update
        fun update(userFavourites: UsersFavourites)

        @Insert
        fun insertAll(userFavourites: List<User>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAllMutable(userFavourites: MutableList<UsersFavourites>?)

        @Delete
        fun delete(userFavourites: UsersFavourites)

}
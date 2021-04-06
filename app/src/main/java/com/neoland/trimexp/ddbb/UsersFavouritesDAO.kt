package com.neoland.trimexp.ddbb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neoland.trimexp.entities.UsersFavourites

@Dao
interface UsersFavouritesDAO {

        @Query("SELECT * FROM UsersFavourites")
        fun getAll(): List<UsersFavourites>

        @Query("SELECT * FROM UsersFavourites")
        fun getAllLive(): LiveData<List<UsersFavourites>>

        @Query("SELECT * FROM UsersFavourites WHERE UsersFavourites.fkuserID = :id")
        fun getUserFavourites(id: Int): List<UsersFavourites>

        @Query("SELECT * FROM UsersFavourites WHERE UsersFavourites.fkuserID = :userId AND UsersFavourites.fkuserFavouriteID = :userFavoriteId")
        fun getUserFavourite(userId: Int, userFavoriteId: Int): UsersFavourites


        @Insert
        fun insert(userFavourites: UsersFavourites)

        @Update
        fun update(userFavourites: UsersFavourites)

        @Insert
        fun insertAll(userFavourites: List<UsersFavourites>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAllMutable(userFavourites: MutableList<UsersFavourites>?)

        @Delete
        fun delete(userFavourites: UsersFavourites)



}
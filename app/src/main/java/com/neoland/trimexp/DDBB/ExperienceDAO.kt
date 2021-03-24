package com.neoland.trimexp.DDBB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.neoland.trimexp.entities.Experience

@Dao
interface ExperienceDAO {

    @Query("SELECT * FROM Experience")
    fun getAll(): List<Experience>

    @Query("SELECT * FROM Experience")
    fun getAllLive(): LiveData<List<Experience>>

    @Insert
    fun insert(experience: Experience)

    @Update
    fun update(experience: Experience)

    @Insert
    fun insertAll(experiences: List<Experience>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMutable(experiences: MutableList<Experience>?)

    @Delete
    fun delete(experience: Experience)
}
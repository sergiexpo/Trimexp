package com.neoland.trimexp.DDBB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DbStatusDao {

    @Query("SELECT * FROM DbStatus")
    fun getAll(): List<DbStatus>

    @Query("SELECT * FROM DbStatus")
    fun getAllLive(): LiveData<List<DbStatus>>

    @Insert
    fun insert(dbStatus: DbStatus)

    @Update
    fun update(dbStatus: DbStatus)

    @Insert
    fun insertAll(dbStatus: List<DbStatus>)

    @Delete
    fun delete(dbStatus: DbStatus)
}
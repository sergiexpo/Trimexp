package com.neoland.trimexp.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(foreignKeys = [ForeignKey(  entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("fkuserID"),
        onDelete = ForeignKey.SET_NULL),
    ForeignKey(entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("fkuserFavouriteID"),
            onDelete = ForeignKey.SET_NULL)]
)

data class UsersFavourites(var fkuserID: Int,
                           var fkuserFavouriteID: Int)
{


    @PrimaryKey(autoGenerate = true)
    var FavouriteId: Int = 0

}
package com.neoland.trimexp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class User (var name: String,
                 var description: String,
                 var email: String,
                 var password: String,
                 var mainPhoto: Int? = null,
                 @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var photoUser: ByteArray? = null

) {

    // Lista de experiencias
    // Lista de valoraciones

    @PrimaryKey(autoGenerate = true)
    var userId : Int = 0
}
package com.neoland.trimexp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (var name: String,
                       var mainPhoto: Int,
                       var description: String,
                       var email: String,
                       var password: String

) {

    // Lista de experiencias
    // Lista de valoraciones

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
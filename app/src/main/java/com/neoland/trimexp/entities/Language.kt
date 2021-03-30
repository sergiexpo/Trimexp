package com.neoland.trimexp.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = arrayOf("userId"),
    childColumns = arrayOf("fkUserId"),
    onDelete = ForeignKey.CASCADE)]
)

data class Language (var languageCode: String, var languageName: String, var fkUserId: Int) {

    @PrimaryKey(autoGenerate = true)
    var languageId : Int = 0
}
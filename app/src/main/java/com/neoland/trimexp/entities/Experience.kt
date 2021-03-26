package com.neoland.trimexp.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = arrayOf("userId"),
    childColumns = arrayOf("fkUserIdOwner"),
    onDelete = ForeignKey.SET_NULL)]
)
data class Experience (var title: String,
                       var mainPhoto: Int,
                       var description: String,
                       var location: String,
                       var adress: String = " ",
                       var latitud: Double,
                       var longitud: Double,
                       var duration: String,
                       var isNovelty: Boolean,
                       var typeExperience: String, // Host o Guest
                       var price: String,
                       var divisa: String = " ",           // Aqui habrá que poner que sea de la clase USER
                       var requester: String?,      // Aqui habrá que poner que sea de la clase USER
                       var dateFrom: Long, //Fecha única
                       var dateTo : Long?,
                       var fkUserIdOwner : Int? = null

) {


  //  var isOpenDate  //Crear variable computada, que se informe con true si hay rango de fechas o false si la fecha es unica

 /*   var opinions: List<String> = listOf()
    var languages: MutableList<String> = mutableListOf()
    var paymentMethods: MutableList<String> = mutableListOf() */

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
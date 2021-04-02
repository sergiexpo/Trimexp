package com.neoland.trimexp.entities

import android.location.Location
import androidx.room.ColumnInfo
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
                       var mainPhoto: Int? = null,
                       var description: String,
                       var location: String? = null,
                       var adress: String = " ",
                       var latitud: Double,
                       var longitud: Double,
                       var duration: String,
                       var isNovelty: Boolean? = null,
                       var typeExperience: String? = null, // Host o Guest
                       var price: String,
                       var currency: String = " ",
                       var paymentType: String? = null,
                       var fkUserIdRequester: Int?,
                       var dateFrom: Long, //Fecha única
                       var dateTo : Long?,
                       var startHour : String? = null,
                       var isReserved: Boolean = false,
                       var fkUserIdOwner : Int? = null,
                       @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var photoExperience: ByteArray? = null

) {


  //  var isOpenDate  //Crear variable computada, que se informe con true si hay rango de fechas o false si la fecha es unica

 /*   var opinions: List<String> = listOf()
    var languages: MutableList<String> = mutableListOf()
    var paymentMethods: MutableList<String> = mutableListOf() */

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0



    fun getDistanceFromUserCurrenLocation(currentLat: Double, currentLong: Double) : Float{

        var currentLocation = Location("Current")
        currentLocation.latitude = currentLat
        currentLocation.longitude = currentLong

        var experienceLocation = Location("Experience")
        experienceLocation.latitude = latitud
        experienceLocation.longitude = longitud

        return currentLocation.distanceTo(experienceLocation)
    }

}
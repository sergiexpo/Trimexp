package com.neoland.trimexp.DDBB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neoland.trimexp.R
import com.neoland.trimexp.entities.Experience
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Experience::class, DbStatus::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun experienceDao(): ExperienceDAO
    abstract fun dbStatusDao(): DbStatusDao

    companion object {

        @Volatile
        private var INSTANCE: Db? = null

        fun getDatabase(context: Context): Db {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return INSTANCE as Db
                }
                val roomBuilder =
                    Room.databaseBuilder(context.applicationContext, Db::class.java, "database-db")
                roomBuilder.addCallback(getCallback())
                INSTANCE = roomBuilder.build()
                return INSTANCE as Db
            }
        }

        private fun getCallback(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val experiences: List<Experience> = listOf(Experience("Human Towers", R.mipmap.sample_castells,
                            "S'anomena diada castellera a una actuació d'una o més colles castelleres davant la presència de públic espectador. Una diada castellera sol " +
                                    "convocar-se a la plaça o carrer més important o emblemàtic del poble o ciutat on s'organitza l'actuació, habitualment on hi té la seu l'ajuntament. " +
                                    "Aquesta plaça o carrer rep el sobrenom de plaça castellera.",
                            "Barcelona", "Plaça Sant Jaume ", "5 horas",  false, "host", "Free",
                             " ", "Laia", null, null, null, "20210924"),
                            Experience( "Partido Barça", R.mipmap.sample_campnou,
                                "El Camp Nou és l'estadi on juga el FC Barcelona, en el barri barceloní de La Maternitat i Sant Ramon, al districte de Les Corts. Té la màxima qualificació (5 estrelles) que la FIFA pot atorgar a un estadi per acollir partits de futbol. El Camp Nou és l'estadi amb més capacitat d'Europa, amb 99.354 espectadors.",
                                "Barcelona", "Les Corts ", "2 horas", false, "host", "50",
                                 "€", "Ronnie", null,null, null, "20210520"  ),
                            Experience( "Bunkers Civil War", R.mipmap.sample_bunker,
                                "Al cim del Turó de la Rovira durant la Guerra Civil espanyola es va instal·lar una bateria antiaèria. L'objectiu era protegir la ciutat de Barcelona de l'aviació feixista italiana que va utilitzar una tàctica sanguinària anomenada \"bombardeig en estora\" (posteriorment aquesta tàctica es va generalitzar durant la Segona Guerra Mundial).",
                                "Barcelona", "Plaça del diamant ", "3 horas", true,  "host", "10",
                                 "€", "Colometa", null,null, null, "20210815"  ),
                            Experience( "Tibidabo", R.mipmap.sample_tibidabo,
                                "Al punt més alt de Barcelona s'hi troba des de fa més d'un segle el Parc d'Atraccions del Tibidabo. Part de la memòria de generacions de barcelonins, continua essent un lloc de diversió, sorpreses i entreteniment, amb atraccions per a petits i grans, per a porucs i atrevits.",
                                "Barcelona", "Muntanya Collserola", "10 horas", true,  "host", "15",
                                 "€", "Laia", null,null, null, "20210924"  ) ,
                            Experience( "Passeig per les rambles", R.mipmap.sample_ramblas,
                                "Las Ramblas siempre están animadas, repletas de turistas y artistas callejeros que actúan como estatuas humanas. El paseo cuenta con numerosas terrazas y resulta agradable sentarse para contemplar el ir y venir de los transeúntes a pesar de que los precios se ven incrementados por tratarse de una zona tan turística.",
                                "Barcelona", "Plaça Catalunya", "2 horas", true,  "host", "Free",
                                 "", "Ronnie", null,null, null, "20210613"  ),
                            Experience( "Visita Museo de Cera", R.mipmap.sample_museucera,
                                "El Nuevo Museo de Cera de Barcelona abre sus puertas convertido en un museo del siglo XXI pero sin perder su impresionante arquitectura e historia. Utilizando nuevas tecnologías, figuras y escenografías distribuidas en 28 zonas, los visitantes disfrutarán de un recorrido por una parte de nuestra historia y de nuestro presente.",
                                "Barcelona", "Ramba Catalunya", "3 horas", false,  "host", "Free",
                                "", "Laia", null,null, null, "20211208" ),
                            Experience( "Escape room Catacumbas", R.mipmap.sample_escaperoom,
                                "Elige película y toma asiento. En cualquier momento sentirás que la ficción se ha transformado en realidad y ahora eres tú el protagonista de la aventura. Llegados a este punto, ya solo te queda por saber que Golden Pop está formado por un equipo de empresas con amplia experiencia en el mundo Room Escape, nuestros juegos están reconocidos dentro del ámbito nacional e incluso contamos con varios premios de prestigio dentro del sector.",
                                "Barcelona", "Carrer girona 23", "1,5 horas", false,  "Guest", "15",
                                "€", "Ronnie", null,null, null, "20210401" )
                            ,
                            Experience( "Visita Kaburi", R.mipmap.sample_kaburi,
                                "¿Juegas a rol? Ella habla tu idioma a la perfección y seguro que sabe encontrar aquella novedad o título que buscas. Ademá puede asesorarte sobre juegos de mesa, manga y anime.",
                                "Barcelona", "Plasseig St Joan", "4 horas", true,  "Guest", "Free",
                                "", "Colometa", null,null, null, "20210331" )
                            )
                        INSTANCE?.experienceDao()?.insertAll(experiences)


                        INSTANCE?.dbStatusDao()?.insert(DbStatus(1, true))


                    }
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                }
            }
        }
    }



}
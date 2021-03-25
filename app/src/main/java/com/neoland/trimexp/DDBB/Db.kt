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
                            "Barcelona", "Plaça Sant Jaume ", "5 horas",  false, "Host", "Free",
                             " ", "Laia", null, 1632477600000, null),
                            Experience( "Partido Barça", R.mipmap.sample_campnou,
                                "El Camp Nou és l'estadi on juga el FC Barcelona, en el barri barceloní de La Maternitat i Sant Ramon, al districte de Les Corts. Té la màxima qualificació (5 estrelles) que la FIFA pot atorgar a un estadi per acollir partits de futbol. El Camp Nou és l'estadi amb més capacitat d'Europa, amb 99.354 espectadors.",
                                "Barcelona", "Les Corts ", "2 horas", false, "Host", "50",
                                 "€", "Ronnie", null,1621504800000, null),
                            Experience( "Bunkers Civil War", R.mipmap.sample_bunker,
                                "Al cim del Turó de la Rovira durant la Guerra Civil espanyola es va instal·lar una bateria antiaèria. L'objectiu era protegir la ciutat de Barcelona de l'aviació feixista italiana que va utilitzar una tàctica sanguinària anomenada \"bombardeig en estora\" (posteriorment aquesta tàctica es va generalitzar durant la Segona Guerra Mundial).",
                                "Barcelona", "Plaça del diamant ", "3 horas", true,  "Host", "10",
                                 "€", "Colometa", null,1629021600000, null),
                            Experience( "Tibidabo", R.mipmap.sample_tibidabo,
                                "Al punt més alt de Barcelona s'hi troba des de fa més d'un segle el Parc d'Atraccions del Tibidabo. Part de la memòria de generacions de barcelonins, continua essent un lloc de diversió, sorpreses i entreteniment, amb atraccions per a petits i grans, per a porucs i atrevits.",
                                "Barcelona", "Muntanya Collserola", "10 horas", true,  "Host", "15",
                                 "€", "Laia", null,1618480800000, null) ,
                            Experience( "Passeig per les rambles", R.mipmap.sample_ramblas,
                                "Las Ramblas siempre están animadas, repletas de turistas y artistas callejeros que actúan como estatuas humanas. El paseo cuenta con numerosas terrazas y resulta agradable sentarse para contemplar el ir y venir de los transeúntes a pesar de que los precios se ven incrementados por tratarse de una zona tan turística.",
                                "Barcelona", "Plaça Catalunya", "2 horas", true,  "Host", "Free",
                                 "", "Ronnie", null,1623578400000, null),
                            Experience( "Visita Museo de Cera", R.mipmap.sample_museucera,
                                "El Nuevo Museo de Cera de Barcelona abre sus puertas convertido en un museo del siglo XXI pero sin perder su impresionante arquitectura e historia. Utilizando nuevas tecnologías, figuras y escenografías distribuidas en 28 zonas, los visitantes disfrutarán de un recorrido por una parte de nuestra historia y de nuestro presente.",
                                "Barcelona", "Ramba Catalunya", "3 horas", false,  "Host", "Free",
                                "", "Laia", null,1639389600000, null),
                            Experience( "Escape room Catacumbas", R.mipmap.sample_escaperoom,
                                "Elige película y toma asiento. En cualquier momento sentirás que la ficción se ha transformado en realidad y ahora eres tú el protagonista de la aventura. Llegados a este punto, ya solo te queda por saber que Golden Pop está formado por un equipo de empresas con amplia experiencia en el mundo Room Escape, nuestros juegos están reconocidos dentro del ámbito nacional e incluso contamos con varios premios de prestigio dentro del sector.",
                                "Barcelona", "Carrer girona 23", "1,5 horas", false,  "Guest", "15",
                                "€", "Ronnie", null,1617271200000, null)
                            ,
                            Experience( "Tour guiado por Gracia", R.mipmap.sample_gracia,
                                "El distrito de Gracia (en catalán, Districte de Gràcia) es uno de los diez distritos en que se divide administrativamente la ciudad de Barcelona. Es el distrito sexto de la ciudad y comprende el territorio de la antigua Villa de Gracia, aglutinada a partir de la parroquia en 1628 y población independiente de Barcelona entre 1821 y 1823 y desde 1850 hasta que fue agregada de nuevo a Barcelona en 1897. Es el distrito más pequeño de Barcelona, con una extensión de 4,19 km², pero es el segundo con mayor densidad demográfica (28 660 habitantes por kilómetro cuadrado), al contar con una población de 120 087 habitantes según los datos del Instituto Nacional de Estadística de España del 1 de enero de 2005.",
                                "Barcelona", "Plaça de la Vila", "3 horas", true,  "Host", "Free",
                                "", "Sergi_Expo", null,1632477600000, null),
                                Experience( "Visita Kaburi", R.mipmap.sample_kaburi,
                                        "¿Juegas a rol? Ella habla tu idioma a la perfección y seguro que sabe encontrar aquella novedad o título que buscas. Ademá puede asesorarte sobre juegos de mesa, manga y anime.",
                                        "Barcelona", "Plasseig St Joan", "4 horas", true,  "Guest", "Free",
                                        "", "Colometa", null,1617184800000, null),
                                Experience( "Espectaculo Magia", R.mipmap.sample_magia,
                                        "Miguel Ángel Alarcón Moreno, que es así como se llama este joven mago, nació el 24 de abril de 1988 en la ciudad de Mollet del Vallès. A diferencia de muchos niños, él nunca tuvo el clásico juego de Magia Borrás, pero el interés y curiosidad por este noble arte siempre estuvo presente dentro de su cabeza. 2028 Pasa años de adolescencia entreteniendo a sus amigos y familiares con los típicos juegos de cartas que todo el mundo hemos hecho alguna vez.",
                                        "Barcelona", "Teatre apolo", "2 horas", true,  "Guest", "10",
                                        "€", "Miguel_Developer", null,1632477600000, null)
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
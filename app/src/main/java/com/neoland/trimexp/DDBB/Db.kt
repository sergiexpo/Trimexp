package com.neoland.trimexp.DDBB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neoland.trimexp.R
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Experience::class, DbStatus::class, User::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun experienceDao(): ExperienceDAO
    abstract fun dbStatusDao(): DbStatusDao
    abstract fun userDAO(): UserDAO

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

                        val users: List<User> = listOf(User("Laia", R.mipmap.user_sample_1, "Viajera callejera", "laia@gmail.com", "patata123" ),
                            User("Goku", R.mipmap.user_sample2, "Time traveller", "ron@gmail.com", "pikachu123" ),
                            User("Luffy", R.mipmap.user_sample3, "Descubrir nuevos lugares", "colometa@gmail.com", "pomelo123" ))

                        INSTANCE?.userDAO()?.insertAll(users)

                        val experiences: List<Experience> = listOf(
                            Experience("Human Towers", R.mipmap.sample_castells,
                                "S'anomena diada castellera a una actuació d'una o més colles castelleres davant la presència de públic espectador. Una diada castellera sol " +
                                        "convocar-se a la plaça o carrer més important o emblemàtic del poble o ciutat on s'organitza l'actuació, habitualment on hi té la seu l'ajuntament. " +
                                        "Aquesta plaça o carrer rep el sobrenom de plaça castellera.",
                                "Barcelona", "Plaça Sant Jaume ", 41.3282744, 2.17699, "5 horas",  false, "Host", "Free",
                                " ",  null, 1632477600000, null, 1),
                            Experience( "Partido Barça", R.mipmap.sample_campnou,
                                "El Camp Nou és l'estadi on juga el FC Barcelona, en el barri barceloní de La Maternitat i Sant Ramon, al districte de Les Corts. Té la màxima qualificació (5 estrelles) que la FIFA pot atorgar a un estadi per acollir partits de futbol. El Camp Nou és l'estadi amb més capacitat d'Europa, amb 99.354 espectadors.",
                                "Barcelona", "Les Corts ", 41.381102, 2.1122828, "2 horas", false, "Host", "50",
                                "€",  null,1621504800000, null, 3),
                            Experience( "Bunkers Civil War", R.mipmap.sample_bunker,
                                "Al cim del Turó de la Rovira durant la Guerra Civil espanyola es va instal·lar una bateria antiaèria. L'objectiu era protegir la ciutat de Barcelona de l'aviació feixista italiana que va utilitzar una tàctica sanguinària anomenada \"bombardeig en estora\" (posteriorment aquesta tàctica es va generalitzar durant la Segona Guerra Mundial).",
                                "Barcelona", "Plaça del diamant ", 41.404176, 2.156273, "3 horas", true,  "Host", "10",
                                "€",  null,1629021600000, null, 2),
                            Experience( "Tibidabo", R.mipmap.sample_tibidabo,
                                "Al punt més alt de Barcelona s'hi troba des de fa més d'un segle el Parc d'Atraccions del Tibidabo. Part de la memòria de generacions de barcelonins, continua essent un lloc de diversió, sorpreses i entreteniment, amb atraccions per a petits i grans, per a porucs i atrevits.",
                                "Barcelona", "Muntanya Collserola",41.423752, 2.118142, "10 horas", true,  "Host", "15",
                                "€",  null,1618480800000, null, 3) ,
                            Experience( "Passeig per les rambles", R.mipmap.sample_ramblas,
                                "Las Ramblas siempre están animadas, repletas de turistas y artistas callejeros que actúan como estatuas humanas. El paseo cuenta con numerosas terrazas y resulta agradable sentarse para contemplar el ir y venir de los transeúntes a pesar de que los precios se ven incrementados por tratarse de una zona tan turística.",
                                "Barcelona", "Plaça Catalunya", 41.385847, 2.169880, "2 horas", true,  "Host", "Free",
                                "",  null,1623578400000, null, 2 ),
                            Experience( "Visita Museo de Cera", R.mipmap.sample_museucera,
                                "El Nuevo Museo de Cera de Barcelona abre sus puertas convertido en un museo del siglo XXI pero sin perder su impresionante arquitectura e historia. Utilizando nuevas tecnologías, figuras y escenografías distribuidas en 28 zonas, los visitantes disfrutarán de un recorrido por una parte de nuestra historia y de nuestro presente.",
                                "Barcelona", "Ramba Catalunya",41.377326, 2.177080, "3 horas", false,  "Host", "Free",
                                "",  null,1639389600000, null, 1),
                            Experience( "Escape room Catacumbas", R.mipmap.sample_escaperoom,
                                "Elige película y toma asiento. En cualquier momento sentirás que la ficción se ha transformado en realidad y ahora eres tú el protagonista de la aventura. Llegados a este punto, ya solo te queda por saber que Golden Pop está formado por un equipo de empresas con amplia experiencia en el mundo Room Escape, nuestros juegos están reconocidos dentro del ámbito nacional e incluso contamos con varios premios de prestigio dentro del sector.",
                                "Barcelona", "Carrer girona 23",41.391091, 2.175964, "1,5 horas", false,  "Guest", "15",
                                "€",  null,1617271200000, null, 1),
                            Experience( "Tour guiado por Gracia", R.mipmap.sample_gracia,
                                "El distrito de Gracia (en catalán, Districte de Gràcia) es uno de los diez distritos en que se divide administrativamente la ciudad de Barcelona. Es el distrito sexto de la ciudad y comprende el territorio de la antigua Villa de Gracia, aglutinada a partir de la parroquia en 1628 y población independiente de Barcelona entre 1821 y 1823 y desde 1850 hasta que fue agregada de nuevo a Barcelona en 1897. Es el distrito más pequeño de Barcelona, con una extensión de 4,19 km², pero es el segundo con mayor densidad demográfica (28 660 habitantes por kilómetro cuadrado), al contar con una población de 120 087 habitantes según los datos del Instituto Nacional de Estadística de España del 1 de enero de 2005.",
                                "Barcelona", "Plaça de la Vila",41.400399, 2.157620, "3 horas", true,  "Host", "Free",
                                "",  null,1632477600000, null,2),
                            Experience( "Visita Kaburi", R.mipmap.sample_kaburi,
                                "¿Juegas a rol? Ella habla tu idioma a la perfección y seguro que sabe encontrar aquella novedad o título que buscas. Ademá puede asesorarte sobre juegos de mesa, manga y anime.",
                                "Barcelona", "Plasseig St Joan",41.392587, 2.178339, "4 horas", true,  "Guest", "Free",
                                "",  null,1617184800000, null,2),
                            Experience( "Espectaculo Magia", R.mipmap.sample_magia,
                                "Miguel Ángel Alarcón Moreno, que es así como se llama este joven mago, nació el 24 de abril de 1988 en la ciudad de Mollet del Vallès. A diferencia de muchos niños, él nunca tuvo el clásico juego de Magia Borrás, pero el interés y curiosidad por este noble arte siempre estuvo presente dentro de su cabeza. 2028 Pasa años de adolescencia entreteniendo a sus amigos y familiares con los típicos juegos de cartas que todo el mundo hemos hecho alguna vez.",
                                "Barcelona", "Teatre apolo",41.374550, 2.169500, "2 horas", true,  "Guest", "10",
                                "€",  null,1632477600000, null,3)
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
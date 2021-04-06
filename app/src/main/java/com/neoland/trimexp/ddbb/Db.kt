package com.neoland.trimexp.ddbb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neoland.trimexp.R
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.entities.User
import com.neoland.trimexp.entities.UsersFavourites
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Experience::class, DbStatus::class, User::class, UsersFavourites::class], version = 1)
abstract class Db : RoomDatabase() {

    abstract fun experienceDao(): ExperienceDAO
    abstract fun dbStatusDao(): DbStatusDao
    abstract fun userDAO(): UserDAO
    abstract fun UsersFavouritesDAO(): UsersFavouritesDAO

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

                        val users: List<User> = listOf(
                                User("Laia", "Barcelona", "Viajera callejera. Me encata viajar, descubrir nuevos lugares y vivr nuevas experiencias",
                                "laia@gmail.com", "laia", R.mipmap.user_sample_1, 4.5F ),
                                User("Goku", "Berlin", "Fiel a su raza, Goku posee un espíritu combativo sumamente marcado, y es incapaz de resistirse a pelear con alguien que le parezca fuerte aun en los momentos más inoportunos. Es extremadamente competitivo y entrena constantemente, ya que nunca está satisfecho con su fuerza actual y siempre busca ir más allá.",
                                        "goku@gmail.com", "goku", R.mipmap.user_sample2, 4F ),
                                User("Luffy", "Madrid", "La personalidad de Luffy es como la de un niño, en el sentido de que tiende a ir a los extremos y él mismo se considera como una persona afortunada al tener su sonrisa. Les gusta descubrir nuevos lugares viajando con su barco",
                                        "luffy@gmail.com", "luffy", R.mipmap.user_sample3, 4F ),
                                User("Iñigo Montoya", "Barcelona", "Hola, me llamo Íñigo Montoya, tú mataste a mi padre, prepárate a morir",
                                        "montoya@gmail.com", "montoya", R.mipmap.user_sample4,4.5F ),
                                User("Sr Orco", "Barcelona", "Me gusta mucho matar humanos. También me gusta comerlos. Podemos quedar cuando quieras. No garantizo que salgas vivo",
                                        "orco@gmail.com", "orco", R.mipmap.user_sample5, 2.5F ),
                                User("Developer Loco", "Barcelona", "Desarrollo programas y juego a videjouegos. Puedes venir a mi casa a jugar al Starcraft o al WoW",
                                        "developer@gmail.com", "developer", R.mipmap.user_sample6, 1F ))

                        INSTANCE?.userDAO()?.insertAll(users)

                        val experiences: List<Experience> = listOf(
                                Experience("Human Towers", R.mipmap.sample_castells,
                                        "S'anomena diada castellera a una actuació d'una o més colles castelleres davant la presència de públic espectador. Una diada castellera sol " +
                                                "convocar-se a la plaça o carrer més important o emblemàtic del poble o ciutat on s'organitza l'actuació, habitualment on hi té la seu l'ajuntament. " +
                                                "Aquesta plaça o carrer rep el sobrenom de plaça castellera.",
                                        "Barcelona", "Plaça Sant Jaume ", 41.382772, 2.177032, 5F,  false, "Host", 0F,
                                        " ",  "Bizum, Cash", null, 1632477600000, null, "12:00h", false,1, 4.5F),
                                Experience( "Partido Barça", R.mipmap.sample_campnou,
                                        "El Camp Nou és l'estadi on juga el FC Barcelona, en el barri barceloní de La Maternitat i Sant Ramon, al districte de Les Corts. Té la màxima qualificació (5 estrelles) que la FIFA pot atorgar a un estadi per acollir partits de futbol. El Camp Nou és l'estadi amb més capacitat d'Europa, amb 99.354 espectadors.",
                                        "Barcelona", "Les Corts ", 41.381102, 2.1122828, 2.5F, false, "Host", 50F,
                                        "€", null, null,1621504800000, null, "12:00h", false,1, 4.5F),
                                Experience( "Bunkers Civil War", R.mipmap.sample_bunker,
                                        "Al cim del Turó de la Rovira durant la Guerra Civil espanyola es va instal·lar una bateria antiaèria. L'objectiu era protegir la ciutat de Barcelona de l'aviació feixista italiana que va utilitzar una tàctica sanguinària anomenada \"bombardeig en estora\" (posteriorment aquesta tàctica es va generalitzar durant la Segona Guerra Mundial).",
                                        "Barcelona", "Plaça del diamant ", 41.404176, 2.156273, 3F, true,  "Host", 10.5F,
                                        "€", "Bizum, Cash", null,1629021600000, null, "12:00h", false,2, 4.5F),
                                Experience( "Tibidabo", R.mipmap.sample_tibidabo,
                                        "Al punt més alt de Barcelona s'hi troba des de fa més d'un segle el Parc d'Atraccions del Tibidabo. Part de la memòria de generacions de barcelonins, continua essent un lloc de diversió, sorpreses i entreteniment, amb atraccions per a petits i grans, per a porucs i atrevits.",
                                        "Barcelona", "Muntanya Collserola",41.423752, 2.118142, 10F, true,  "Host", 15F,
                                        "€", "Bizum, Cash", null,1618480800000, null, "12:00h", false,2, 4.5F) ,
                                Experience( "Passeig per les rambles", R.mipmap.sample_ramblas,
                                        "Las Ramblas siempre están animadas, repletas de turistas y artistas callejeros que actúan como estatuas humanas. El paseo cuenta con numerosas terrazas y resulta agradable sentarse para contemplar el ir y venir de los transeúntes a pesar de que los precios se ven incrementados por tratarse de una zona tan turística.",
                                        "Barcelona", "Plaça Catalunya", 41.385847, 2.169880, 4F, true,  "Host", 0F,
                                        "", null, null,1623578400000, null, "12:00h", false,3, 4F ),
                                Experience( "Visita Museo de Cera", R.mipmap.sample_museucera,
                                        "El Nuevo Museo de Cera de Barcelona abre sus puertas convertido en un museo del siglo XXI pero sin perder su impresionante arquitectura e historia. Utilizando nuevas tecnologías, figuras y escenografías distribuidas en 28 zonas, los visitantes disfrutarán de un recorrido por una parte de nuestra historia y de nuestro presente.",
                                        "Barcelona", "Ramba Catalunya",41.377326, 2.177080, 3F, false,  "Host", 0F,
                                        "", null, null,1639389600000, null, "12:00h", false,3, 4F),
                                Experience( "Escape room Catacumbas", R.mipmap.sample_escaperoom,
                                        "Elige película y toma asiento. En cualquier momento sentirás que la ficción se ha transformado en realidad y ahora eres tú el protagonista de la aventura. Llegados a este punto, ya solo te queda por saber que Golden Pop está formado por un equipo de empresas con amplia experiencia en el mundo Room Escape, nuestros juegos están reconocidos dentro del ámbito nacional e incluso contamos con varios premios de prestigio dentro del sector.",
                                        "Barcelona", "Carrer girona 23",41.391091, 2.175964, 1.5F, false,  "Guest", 15F,
                                        "€","Bizum, Cash",  null,1617271200000, null, "12:00h", false,4, 4F),
                                Experience( "Tour guiado por Gracia", R.mipmap.sample_gracia,
                                        "El distrito de Gracia (en catalán, Districte de Gràcia) es uno de los diez distritos en que se divide administrativamente la ciudad de Barcelona. Es el distrito sexto de la ciudad y comprende el territorio de la antigua Villa de Gracia, aglutinada a partir de la parroquia en 1628 y población independiente de Barcelona entre 1821 y 1823 y desde 1850 hasta que fue agregada de nuevo a Barcelona en 1897. Es el distrito más pequeño de Barcelona, con una extensión de 4,19 km², pero es el segundo con mayor densidad demográfica (28 660 habitantes por kilómetro cuadrado), al contar con una población de 120 087 habitantes según los datos del Instituto Nacional de Estadística de España del 1 de enero de 2005.",
                                        "Barcelona", "Plaça de la Vila",41.400399, 2.157620, 3F, true,  "Host", 0F,
                                        "", null, null,1632477600000, null,"12:00h", false,4, 4F),
                                Experience( "Visita Kaburi", R.mipmap.sample_kaburi,
                                        "¿Juegas a rol? Ella habla tu idioma a la perfección y seguro que sabe encontrar aquella novedad o título que buscas. Ademá puede asesorarte sobre juegos de mesa, manga y anime.",
                                        "Barcelona", "Plasseig St Joan",41.392587, 2.178339, 4F, true,  "Guest", 0F,
                                        "", null, null,1617184800000, null,"12:00h", false,5, 4F),
                                Experience( "Espectaculo Magia", R.mipmap.sample_magia,
                                        "Miguel Ángel Alarcón Moreno, que es así como se llama este joven mago, nació el 24 de abril de 1988 en la ciudad de Mollet del Vallès. A diferencia de muchos niños, él nunca tuvo el clásico juego de Magia Borrás, pero el interés y curiosidad por este noble arte siempre estuvo presente dentro de su cabeza. 2028 Pasa años de adolescencia entreteniendo a sus amigos y familiares con los típicos juegos de cartas que todo el mundo hemos hecho alguna vez.",
                                        "Barcelona", "Teatre apolo",41.374550, 2.169500, 2F, true,  "Guest", 10.5F,
                                        "€", "Bizum, Cash", null,1632477600000, null,"12:00h", false,5, 5F),
                                Experience( "Tour Barcelona romana", R.mipmap.sample_tour_bcn_romana,
                                        "Las murallas romanas que rodean el corazón de Barcelona abarcan la Barcino que existió entre el siglo I aC y los inicios de la Edad Media. Una ciudad que nació como una pequeña colonia y que creció poco a poco hasta convertirse en, durante un período breve de tiempo, ciudad imperial. Recorre sus calles y descubre donde se ubicaba el fórum, los acueductos, las calles principales, etc.",
                                        "Barcelona", "Plaça Sant Jaume ", 41.382830, 2.177017, 2.5F, false, "Host", 10F,
                                        "€","Bizum, Cash",  null,1632477600000, null, "12:00h", false,3, 5F),
                                Experience( "Tour de tapas en la Boqueria", R.mipmap.sample_tour_tapas_boqueria,
                                        "La Boquería es un mercado abierto y un referente de Barcelona. Está en el corazón de la ciudad. Además de vender todo tipo de productos frescos y de km0 la Boqueria cuenta con varios bares para degustar las mejores tapas de la ciudad. Disfruta de esta pequeña guía que te llevará a los mejores bares del mercado y te ayudará a comprar productos frescos y de calidad.",
                                        "Barcelona", "La Boqueria", 41.38161, 2.171596, 3F, false, "Host", 0F,
                                        "€",null, null, 1629021600000,null, "12:00h", false,2, 5F),
                                Experience( "Calçotada en Montjuïc", R.mipmap.sample_montjuic,
                                        "El Mirador del Migdia es un merendero situado en una de los lugares más emblemáticos y que ofrece mejores vistas de Barcelona: Montjuïc. Aprende a cocinar y comer calçots. Disfruta de la gastronomíaa catalana y de los paisajes que ofrece la montaña de la ciudad de Barcelona.",
                                        "Barcelona", "Mirador del Migdia", 41.358847, 2.159522, 4.5F, false, "Host", 0F,
                                        "€", "Bizum, Cash", null,1639389600000, null, "12:00h", false,1, 3.5F),
                                Experience( "Volley en la playa", R.mipmap.sample_volley,
                                        "¿Te gustan los deportes? ¿Te gusta el volley? ¿Te gustaría disfrutar de las playas de Barcelona? Ven con nosotros a jugar unos partidos de volley en la playa de Nova Icaria y aprovecha que estás cerca del agua para darte un buen baño en el mar, mientras descansamos.",
                                        "Barcelona", "Playa de Nova Icaria", 41.390163, 2.201465, 1.5F, false, "Host", 0F,
                                        "€",null,null,  1617271200000,null, "12:00h", false,6, 3.5F),
                                Experience( "Ruta modernista por Passeig de Gràcia", R.mipmap.sample_tour_modernista,
                                        "Una de las calles más famosas y comerciales de Barcelona esconde un fuerte pasado modernista que va más allá de la Pedrera y la Casa Batlló. En este tour que empieza a Jardinets de Gracia, descubriremos varias casas que pertenecieron a familias importantes de Barcelona, así como diferentes curiosidades relacionadas con el Modernismo",
                                        "Barcelona", "Jardins de Salvador Espriu", 41.397511, 2.158182, 5F, false, "Host", 0F,
                                        "€",null,  null,1639389600000, null, "12:00h", false,6, 3F),
                                Experience( "Partida Starcraft", R.mipmap.sample_starcraft,
                                        "StarCraft es un juego de estrategia en tiempo real. Sus acontecimientos tienen lugar en un sector lejano de la galaxia Vía Láctea. Es el primer videojuego StarCraft y fue puesto a la venta para PC el 1 de abril de 1998. Una versión Mac OS del juego fue lanzada por Blizzard Entertainment en marzo de 1999.",
                                        "Barcelona", "Arc de triomf", 41.391120, 2.179895, 12F, false, "Host", 0F,
                                        "",null, null,1629021600000, null, "22:00h", false,6, 3F),
                                Experience( "Partida Warcraft", R.mipmap.sample_wow,
                                        "Descubre Azeroth tal y como era para una experiencia realmente auténtica. Una recreación de World of Warcraft original para disfrutar de una experiencia auténtica. Blizzard Entertainment. Marcas: Shadowlands, Battle for Azeroth, Warlords of Draenor.",
                                        "Barcelona", "Parc Ciutadella", 41.388304, 2.184130, 12F, false, "Host", 0F,
                                        "",null, null,1629021600000, null, "22:00h", false,6, 3F)


                        )


                        INSTANCE?.experienceDao()?.insertAll(experiences)

                        val usersFavourites: List<UsersFavourites> = listOf(UsersFavourites(1,2),
                                UsersFavourites(1,3),
                                UsersFavourites(1,4)
                        )

                        INSTANCE?.UsersFavouritesDAO()?.insertAll(usersFavourites)

                        INSTANCE?.dbStatusDao()?.insert(DbStatus(1, true))


                    }
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                }
            }
        }
    }



}
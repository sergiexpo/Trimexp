package com.neoland.trimexp.experiences.explorer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.CardExperienceBinding
import com.neoland.trimexp.entities.Experience

interface ExplorerExperiencesAdapterInterface{
    fun onItemClick(experience: Experience)
}

class ExplorerExperiencesAdapter(val listener : ExplorerExperiencesAdapterInterface) : RecyclerView.Adapter<ExplorerExperiencesAdapter.ExperienceViewHolder>()  {

  //  var experiences = listOf<Experience>()
    var experiences = listOf(Experience("Human Towers", R.mipmap.sample_castells,
                                        "S'anomena diada castellera a una actuació d'una o més colles castelleres davant la presència de públic espectador. Una diada castellera sol " +
                                                    "convocar-se a la plaça o carrer més important o emblemàtic del poble o ciutat on s'organitza l'actuació, habitualment on hi té la seu l'ajuntament. " +
                                                    "Aquesta plaça o carrer rep el sobrenom de plaça castellera.",
                                        "Barcelona", "Plaça Sant Jaume ", "5 horas", mutableListOf<String>(), false, "host", mutableListOf<String>(),"Free",
                                        mutableListOf<String>(), " ", "Laia", null ),
                            Experience( "Partido Barça", R.mipmap.sample_campnou,
                                        "El Camp Nou és l'estadi on juga el FC Barcelona, en el barri barceloní de La Maternitat i Sant Ramon, al districte de Les Corts. Té la màxima qualificació (5 estrelles) que la FIFA pot atorgar a un estadi per acollir partits de futbol. El Camp Nou és l'estadi amb més capacitat d'Europa, amb 99.354 espectadors.",
                                        "Barcelona", "Les Corts ", "2 horas", mutableListOf<String>(), true, "host", mutableListOf<String>(),"50",
                                        mutableListOf<String>(), "€", "Ronnie", null ),
                            Experience( "Bunkers Civil War", R.mipmap.sample_bunker,
                                        "Al cim del Turó de la Rovira durant la Guerra Civil espanyola es va instal·lar una bateria antiaèria. L'objectiu era protegir la ciutat de Barcelona de l'aviació feixista italiana que va utilitzar una tàctica sanguinària anomenada \"bombardeig en estora\" (posteriorment aquesta tàctica es va generalitzar durant la Segona Guerra Mundial).",
                                        "Barcelona", "Plaça del diamant ", "3 horas", mutableListOf<String>(), true, "host", mutableListOf<String>(),"10",
                                        mutableListOf<String>(), "€", "Colometa", null ),
            Experience( "Tibidabo", R.mipmap.sample_tibidabo,
                    "Al punt més alt de Barcelona s'hi troba des de fa més d'un segle el Parc d'Atraccions del Tibidabo. Part de la memòria de generacions de barcelonins, continua essent un lloc de diversió, sorpreses i entreteniment, amb atraccions per a petits i grans, per a porucs i atrevits.",
                    "Barcelona", "Muntanya Collserola", "10 horas", mutableListOf<String>(), false, "host", mutableListOf<String>(),"15",
                    mutableListOf<String>(), "€", "Laia", null ) ,
            Experience( "Passeig per les rambles", R.mipmap.sample_ramblas,
                    "Las Ramblas siempre están animadas, repletas de turistas y artistas callejeros que actúan como estatuas humanas. El paseo cuenta con numerosas terrazas y resulta agradable sentarse para contemplar el ir y venir de los transeúntes a pesar de que los precios se ven incrementados por tratarse de una zona tan turística.",
                    "Barcelona", "Plaça Catalunya", "2 horas", mutableListOf<String>(), false, "host", mutableListOf<String>(),"Free",
                    mutableListOf<String>(), "", "Ronnie", null )

    )

    class ExperienceViewHolder(val itemBinding: CardExperienceBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemBinding = CardExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {

        var experience = experiences[position]

        holder.itemBinding.textViewTitleExp.text = experience.title
        holder.itemBinding.imageViewPhotoExp.setImageResource(experience.mainPhoto)
        holder.itemBinding.textViewLocation.text = experience.location
        holder.itemBinding.textViewDuration.text = experience.duration
        holder.itemBinding.textViewPrice.text = experience.price + " " + experience.divisa


        holder.itemBinding.root.setOnClickListener {
            listener.onItemClick(experiences[position])
        }

    }

    override fun getItemCount(): Int {
        return experiences.size
    }


}
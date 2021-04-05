package com.neoland.trimexp.experiences.explorer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.neoland.trimexp.databinding.FragmentExperienceslistBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.detail.ExperienceDetailActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG31
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG32
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG20
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG10
import kotlinx.coroutines.launch

class ExplorerExperiencesFragment: Fragment(),  ExplorerExperiencesAdapterInterface {

    private lateinit var binding: FragmentExperienceslistBinding
    private lateinit var model : ExplorerExperiencesFragmentViewModel
    private var adapter = ExplorerExperiencesAdapter(this)

    private var date: Long = 0L

    private var lat = 0.0
    private var long = 0.0

    enum class SortTypes {
        BY_NAME_ASCENDING,
        BY_NAME_DESCENDING,
        BY_DISTANCE,
        BY_DURATION
    }

    enum class FilterTypes {
        FROM_DATE,
        TO_DATE,
        FREE,
        ALL,
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(ExplorerExperiencesFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperienceslistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = arguments?.getLong("LONG") ?: 0
        lat = arguments?.getDouble("LATITUD") ?: 0.0
        long = arguments?.getDouble("LONGITUD") ?: 0.0

        createRecyclerView()

        observeModelExperiences()
    }

    override fun onResume() {
        super.onResume()
        explorerExperiences()
    }


    private fun createRecyclerView() {
        adapter.lat = lat
        adapter.long = long
        binding.recyclerViewExperiences.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewExperiences.adapter = adapter
    }

    private fun observeModelExperiences(){
        lifecycleScope.launch {
            model.experiences.observe(viewLifecycleOwner) {
                adapter.updateExperiences(it)
            }
        }
    }

    fun sortExperiences(sortType : SortTypes) {
        lifecycleScope.launchWhenResumed {
            when (sortType) {
                SortTypes.BY_NAME_ASCENDING -> model.sortExperiencesByAscendingName()
                SortTypes.BY_NAME_DESCENDING -> model.sortExperiencesByDescendingName()
                SortTypes.BY_DISTANCE -> model.sortExperiencesByDistance(lat, long)
                SortTypes.BY_DURATION -> model. sortExperiencesByDuration()

            }
        }
    }

    fun filterExperiences(filterType : FilterTypes){
        lifecycleScope.launchWhenResumed {      //Esperamos a que el onCreate haya finalizado
            when (filterType){
                FilterTypes.FROM_DATE -> model.filterExperiencesFromDate(date)
                FilterTypes.FREE -> model.filterExperiencesFree()
                FilterTypes.ALL -> model.filterExperiencesAll(date)

            }
        }

    }

    fun explorerExperiences(){
        lifecycleScope.launchWhenResumed {
            model.getExplorerExperiencesList(lat, long, date)
        }
    }


    private fun startExperienceDetailActivity(experience: Experience){

        activity?.let{
            val intent = Intent(it, ExperienceDetailActivity::class.java)

            intent.putExtra(TAG10, experience.id)
            intent.putExtra(TAG20, experience.fkUserIdOwner)
            intent.putExtra(TAG31, lat)
            intent.putExtra(TAG32, long)

            it.startActivity(intent)
        }

    }

    override fun onItemClick(experience: Experience) {
        startExperienceDetailActivity(experience)
    }


}
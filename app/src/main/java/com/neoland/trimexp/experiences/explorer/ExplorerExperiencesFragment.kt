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
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG1
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG10
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG11
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG12
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG2
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG3
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG4
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG6
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG7
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG8
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG9
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapter
import kotlinx.coroutines.launch

class ExplorerExperiencesFragment: Fragment(),  ExplorerExperiencesAdapterInterface {

    private lateinit var binding: FragmentExperienceslistBinding
    private lateinit var model : ExplorerExperiencesFragmentViewModel
    private var adapter = ExplorerExperiencesAdapter(this)

    enum class SortTypes {
        BY_NAME_ASCENDING,
        BY_NAME_DESCENDING,
        BY_DISTANCE,
        BY_DATES
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

        createRecyclerView()

        observeModelExperiences()

    }

    override fun onResume() {
        super.onResume()
      //  model.getAllExperiences()
    }


    private fun createRecyclerView() {
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
        lifecycleScope.launchWhenCreated {
            when (sortType) {
                SortTypes.BY_NAME_ASCENDING -> model.sortExperiencesByAscendingName()
                SortTypes.BY_NAME_DESCENDING -> model.sortExperiencesByDescendingName()

            }
        }
    }

    fun filterExperiences(filterType : FilterTypes, date: Long = 0){
        lifecycleScope.launchWhenCreated {      //Esperamos a que el onCreate haya finalizado
            when (filterType){
                FilterTypes.FROM_DATE -> model.filterExperiencesFromDate(date)
                FilterTypes.FREE -> model.filterExperiencesFree()
                FilterTypes.ALL -> model.filterExperiencesAll(date)

            }
        }

    }


    private fun startExperienceDetailActivity(experience: Experience){

        activity?.let{
            val intent = Intent(it, ExperienceDetailActivity::class.java)
            intent.putExtra(TAG1, experience.mainPhoto)
            intent.putExtra(TAG2, experience.title)
            intent.putExtra(TAG3, experience.description)
            intent.putExtra(TAG4, experience.dateFrom)
            intent.putExtra(TAG6, experience.duration)
            intent.putExtra(TAG9, experience.price)
            intent.putExtra(TAG10, experience.divisa)
            intent.putExtra(TAG11, experience.adress)
            intent.putExtra(TAG12, experience.fkUserId)
            it.startActivity(intent)
        }

    }

    override fun onItemClick(experience: Experience) {
        startExperienceDetailActivity(experience)
    }


}
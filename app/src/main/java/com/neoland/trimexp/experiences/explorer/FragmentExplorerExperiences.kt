package com.neoland.trimexp.experiences.explorer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.neoland.trimexp.databinding.FragmentExperienceslistBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.detail.ExperienceDetailActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapter

class FragmentExplorerExperiences: Fragment(),  ExplorerExperiencesAdapterInterface {

    private lateinit var binding: FragmentExperienceslistBinding
    private var adapter = ExplorerExperiencesAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperienceslistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRecyclerView()

    }



    private fun createRecyclerView() {
        binding.recyclerViewExperiences.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewExperiences.adapter = adapter
    }

    private fun startExperienceDetailActivity(){

        activity?.let{
            val intent = Intent(it, ExperienceDetailActivity::class.java)
            it.startActivity(intent)
        }

    }

    override fun onItemClick(experience: Experience) {
        startExperienceDetailActivity()
    }


}
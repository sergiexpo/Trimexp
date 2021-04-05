package com.neoland.trimexp.experiences.userlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.neoland.trimexp.databinding.FragmentExperienceslistBinding
import com.neoland.trimexp.databinding.FragmentExperiencesuserlistBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.detail.ExperienceDetailActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesAdapter
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesFragment
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListExperiencesFragment : Fragment(),  UserListExperiencesAdapterInterface {

    private lateinit var binding: FragmentExperiencesuserlistBinding
    private lateinit var model : UserListExperienceFragmentViewModel
    private lateinit var adapter: UserListExperienceAdapter

    private var userId : Int? = null

    enum class FilterExperiencesUserListTypes {
        OPEN,
        HISTORICAL,
        WISH
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(UserListExperienceFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperiencesuserlistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch(Dispatchers.Main) {
            model.loadPreferences("TAG_EMAIL")?.let{ email ->
                if (email.isNotEmpty()) {
                    val user = model.getUser(email)
                    userId = user.userId
                    showExperiencesUserlist()
                } else {
                    model.loadPreferences("TAG_EMAIL_TEMPORAL")?.let { email_temp ->
                        if (email_temp.isNotEmpty()) {
                            val user = model.getUser(email_temp)
                            userId = user.userId
                            showExperiencesUserlist()
                        } else {
                            Toast.makeText(binding.root.context, "Please, log in the app", Toast.LENGTH_LONG).show()
                            //Mejor finalizar el fragment y reedirigir
                        }
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        getExperiencesUserList()
    }


    private fun createRecyclerView() {

        userId?.let { id ->
            getContext()?.let { context ->
                adapter = UserListExperienceAdapter(this, id, context)
            }
        }

        binding.recyclerViewExperiencesListUser.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewExperiencesListUser.adapter = adapter
    }

    private fun observeModelExperiences(){
        lifecycleScope.launch {
            model.experiences.observe(viewLifecycleOwner) {
                adapter.updateExperiences(it)
            }
        }
    }



    fun getExperiencesUserList(){
        lifecycleScope.launchWhenResumed {
            userId?.let{
            model.filterExperiencesOpen(it)
        }
        }
    }

    fun showExperiencesUserlist(){
        getExperiencesUserList()
        createRecyclerView()
        observeModelExperiences()
    }



    fun filterExperiences(filterType : FilterExperiencesUserListTypes){
        lifecycleScope.launchWhenResumed {
            when (filterType){
                FilterExperiencesUserListTypes.OPEN -> userId?.let{model.filterExperiencesOpen(it)}
                FilterExperiencesUserListTypes.HISTORICAL -> userId?.let{model.filterExperiencesHistorical(it)}
          //    FilterExperiencesUserListTypes.WISH ->
            }
        }
    }




    private fun startExperienceDetailActivity(experience: Experience){

        activity?.let{
            val intent = Intent(it, ExperienceDetailActivity::class.java)

            intent.putExtra(ExplorerExperiencesActivity.TAG10, experience.id)
            intent.putExtra(ExplorerExperiencesActivity.TAG20, experience.fkUserIdOwner)

            it.startActivity(intent)
        }

    }

    override fun onItemClick(experience: Experience) {
        startExperienceDetailActivity(experience)
    }

}
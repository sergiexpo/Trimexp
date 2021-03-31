package com.neoland.trimexp.experiences.manage

import android.app.AlertDialog
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
import com.neoland.trimexp.databinding.FragmentExperiencesuserlistBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.detail.ExperienceDetailActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageExperienceFragment : Fragment(), ManageExperienceAdapterInterface {

    private lateinit var binding: FragmentExperiencesuserlistBinding
    private lateinit var model : ManageExperienceFragmentViewModel
    private lateinit var adapter: ManageExperienceAdapter
   // private var userId : Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(ManageExperienceFragmentViewModel::class.java)
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
                    val userId = user.userId
                     showExperiencesUserlist(userId)
                } else {
                    model.loadPreferences("TAG_EMAIL_TEMPORAL")?.let { email_temp ->
                        if (email_temp.isNotEmpty()) {
                            val user = model.getUser(email_temp)
                            val userId = user.userId
                            showExperiencesUserlist(userId)
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
        // getExperiencesUserList(userId)
    }


    private fun createRecyclerView(userId: Int) {

        adapter = ManageExperienceAdapter(this, userId)
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



    fun getExperiencesUserList(userId: Int) {
        model.getExperiencesOpen(userId)
    }

    fun showExperiencesUserlist(userId: Int) {
        getExperiencesUserList(userId)
        createRecyclerView(userId)
        observeModelExperiences()
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

    override fun onLongItemClick(experience: Experience, userId: Int){

        val builder = AlertDialog.Builder(binding.root.context)

        builder.setMessage("User will be removed. Are you sure?")

        builder.setPositiveButton("Yes"){ dialog, id ->
            model.deleteExperience(experience, userId)

        }
        builder.setNegativeButton("Cancel"){ dialog, id ->
        }
        builder.create()
        builder.show()
    }




}
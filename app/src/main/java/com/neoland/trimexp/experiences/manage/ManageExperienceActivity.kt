package com.neoland.trimexp.experiences.manage

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.databinding.ActivityExperienceUserManageBinding


class ManageExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExperienceUserManageBinding
    private var fragment = ManageExperienceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceUserManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment(fragment)

        binding.imageViewIconBack.setOnClickListener {
            goBackManageExperienceActivity()
        }


        binding.swSearchExp.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    fragment.searchExperiencesInManagerList(p0)
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    fragment.searchExperiencesInManagerList(p0)
                }
                return true
            }
        })

        binding.imageViewIconReload.setOnClickListener {
            fragment.reloadExperiencesInManagerList()
        }



    }

    private fun goBackManageExperienceActivity() {
        onBackPressed()
    }


    private fun initFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayoutExperiences.id, fragment)
        fragmentTransaction.commit()

    }

}
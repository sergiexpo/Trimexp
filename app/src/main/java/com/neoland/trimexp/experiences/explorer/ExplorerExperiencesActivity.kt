package com.neoland.trimexp.experiences.explorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding


class ExplorerExperiencesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExplorerExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationViewMenuExperiencesList.setOnNavigationItemSelectedListener{ itemSelected ->
            when (itemSelected.itemId){

                R.id.option_sort-> {
                changeFragment(FragmentExplorerExperiences())
                }
                R.id.option_filter -> {

                }
                R.id.option_map -> {

                }
            }
            true

            }

        }



    private fun changeFragment(fragment : Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayoutExperiences.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}
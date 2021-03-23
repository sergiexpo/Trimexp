package com.neoland.trimexp.experiences.explorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding


class ExplorerExperiencesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExplorerExperienceBinding

    companion object {
        const val TAG1 = "Photo"
        const val TAG2 = "Title"
        const val TAG3 = "Description"
        const val TAG4 = "Date"
        const val TAG5 = "Time"
        const val TAG6 = "Duration"
        const val TAG7 = "Language"
        const val TAG8 = "Payment Methods"
        const val TAG9 = "Price"
        const val TAG10 = "Divisa"
        const val TAG11 = "Adress"
        const val TAG12 = "Owner"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationViewMenuExperiencesList.setOnNavigationItemSelectedListener{ itemSelected ->
            when (itemSelected.itemId){

                R.id.option_sort-> {
                    changeFragment(ExplorerExperiencesFragment())
                }
                R.id.option_filter -> {
                    changeFragment(ExplorerExperiencesFragment())
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
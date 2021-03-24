package com.neoland.trimexp.experiences.explorer

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding
import kotlinx.coroutines.delay


class ExplorerExperiencesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExplorerExperienceBinding
    private var fragment = ExplorerExperiencesFragment()


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

        changeFragment(fragment)

        binding.bottomNavigationViewMenuExperiencesList.setOnNavigationItemSelectedListener{ itemSelected ->
            when (itemSelected.itemId){

                R.id.option_sort-> {
                  //fragment.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_ASCENDING)
                    showDialog("aa")
                }
                R.id.option_filter -> {
                    fragment.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_DESCENDING)
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



    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.options_menu_sort)
      //  val body = dialog.findViewById(R.id.body) as TextView
      //  body.text = title
        val sortButtonAscName = dialog.findViewById(R.id.option_sort_AscendingName) as RadioButton
        val sortButtonDescName = dialog.findViewById(R.id.option_sort_DescendingName) as RadioButton

        sortButtonAscName.setOnClickListener {
            dialog.dismiss()
            fragment.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_ASCENDING)
        }
        sortButtonDescName.setOnClickListener {
            dialog.dismiss()
            fragment.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_DESCENDING)
        }
        dialog.show()
    }


}
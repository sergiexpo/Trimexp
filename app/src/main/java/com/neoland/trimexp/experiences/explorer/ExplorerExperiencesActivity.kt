package com.neoland.trimexp.experiences.explorer

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding
import com.neoland.trimexp.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ExplorerExperiencesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExplorerExperienceBinding
    private var fragment = ExplorerExperiencesFragment()
    private var date : Long = 0

    companion object {
        const val TAG1 = "Photo"
        const val TAG2 = "Title"
        const val TAG3 = "Description"
        const val TAG4 = "Date"
        const val TAG45 = "Date_From"
        const val TAG46 = "Date_To"
        const val TAG5 = "Time"
        const val TAG6 = "Duration"
        const val TAG7 = "Language"
        const val TAG8 = "Payment Methods"
        const val TAG9 = "Price"
        const val TAG10 = "Divisa"
        const val TAG11 = "Adress"
        const val TAG12 = "Owner"
        const val TAG20 = "UniqueDateFromHomeScreen"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        date = intent.getLongExtra(TAG20, 0L)

        binding.textViewSearch.text = convertLongToFormattedDate(date)

        lifecycleScope.launch {
            lifecycleScope.async(Dispatchers.IO)  {
                changeFragment(fragment)
            } .await()
            fragment.filterExperiences(ExplorerExperiencesFragment.FilterTypes.FROM_DATE, date )
        }


        binding.imageViewIconBack.setOnClickListener {
            goBackHomeActivity()
        }


        binding.bottomNavigationViewMenuExperiencesList.setOnNavigationItemSelectedListener{ itemSelected ->
            when (itemSelected.itemId){

                R.id.option_sort-> {
                    showDialogSort()
                }
                R.id.option_filter -> {
                    showDialogFilter()
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
      //  fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun showDialogSort() {
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


    private fun showDialogFilter() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.options_menu_filter)

        val filterButtonAll = dialog.findViewById(R.id.option_filter_all) as RadioButton
        val filterButtonFree = dialog.findViewById(R.id.option_filter_free) as RadioButton

        filterButtonAll.setOnClickListener {
            dialog.dismiss()
            fragment.filterExperiences(ExplorerExperiencesFragment.FilterTypes.ALL, date)
        }

        filterButtonFree.setOnClickListener {
            dialog.dismiss()
            fragment.filterExperiences(ExplorerExperiencesFragment.FilterTypes.FREE)
        }

        dialog.show()
    }



    private fun goBackHomeActivity(){
        onBackPressed()
    }



    private fun convertLongToFormattedDate(time: Long): String {
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("EEE., dd MMM. yyyy")
        return simpleDateFormat.format(date.time)
    }

}
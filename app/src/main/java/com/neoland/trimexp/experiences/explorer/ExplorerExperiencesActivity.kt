package com.neoland.trimexp.experiences.explorer

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityExplorerExperienceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ExplorerExperiencesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityExplorerExperienceBinding
    private var fragmentList = ExplorerExperiencesFragment()
    private var fragmentMap = ExplorerExperiencesMapFragment()
    private var date : Long = 0

    private var lat = 0.0
    private var long = 0.0
    private var location = LatLng(0.0, 0.0)

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
        const val TAG98 = "Lat"
        const val TAG99 = "Long"
        const val TAG100 = "Current Lat"
        const val TAG101 = " Current Long"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        date = intent.getLongExtra(TAG20, 0L)

        lat = 41.408141                 //TEMPORAL
        long = 2.158466                 //TEMPORAL
        location = LatLng(lat, long)    //TEMPORAL



        binding.textViewSearch.text = convertLongToFormattedDate(date)

        lifecycleScope.launch {
            lifecycleScope.async(Dispatchers.IO)  {
                fragmentList.arguments = Bundle().apply {
                    putLong("LONG", date)
                    putDouble("LATITUD", lat)
                    putDouble("LONGITUD", long)}
                changeFragment(fragmentList)
            } .await()
            //fragmentList.filterExperiences(ExplorerExperiencesFragment.FilterTypes.FROM_DATE)
            fragmentList.explorerExperiences()
        }


        binding.imageViewIconBack.setOnClickListener {
            goBackHomeActivity()
        }


        binding.bottomNavigationViewMenuExperiencesList.setOnNavigationItemSelectedListener{ itemSelected ->
            when (itemSelected.itemId){

                R.id.option_sort-> {
                    if (!fragmentList.isVisible) {
                        changeFragment(fragmentList)
                    }
                    showDialogSort()
                }
                R.id.option_filter -> {
                    if (!fragmentList.isVisible) {
                        changeFragment(fragmentList)
                    }
                    showDialogFilter()
                }
                R.id.option_map -> {
                    if (!fragmentMap.isVisible) {
                        changeFragment(fragmentMap)
                    }
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
            fragmentList.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_ASCENDING)
        }
        sortButtonDescName.setOnClickListener {
            dialog.dismiss()
            fragmentList.sortExperiences(ExplorerExperiencesFragment.SortTypes.BY_NAME_DESCENDING)
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
            fragmentList.filterExperiences(ExplorerExperiencesFragment.FilterTypes.ALL)
        }

        filterButtonFree.setOnClickListener {
            dialog.dismiss()
            fragmentList.filterExperiences(ExplorerExperiencesFragment.FilterTypes.FREE)
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
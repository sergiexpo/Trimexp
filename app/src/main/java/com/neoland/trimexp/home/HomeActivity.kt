package com.neoland.trimexp.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.neoland.trimexp.databinding.ActivityHomeBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesFragment
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity: AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var model: HomeActivityViewModel
    private var dateUnique: Long = 0




    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())

        binding.imageViewIconMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.editTextDates.setText("${formatDate(Date())}")

        binding.editTextDates.setOnClickListener(){

                val builder = MaterialDatePicker.Builder.dateRangePicker()
                builder.setSelection(androidx.core.util.Pair(System.currentTimeMillis(), System.currentTimeMillis()))
                val picker = builder.build()
                picker.show(supportFragmentManager, picker.toString())
                picker.addOnNegativeButtonClickListener { picker.dismiss() }
                picker.addOnPositiveButtonClickListener {
                    val calendar1 = Calendar.getInstance()
                    val calendar2 = Calendar.getInstance()

                    val date1 = it.first?.let { it1 -> Date(it1) }
                    val date2 = it.second?.let { it2 -> Date(it2) }
                    calendar1.time = date1
                    calendar2.time = date2

                    if (date1 != null && date2 != null && date1 != date2) {

                        binding.editTextDates.setText("${formatDate(date1)} - ${formatDate(date2)}")
                        dateUnique = calendar1.timeInMillis

                    } else if (date1 != null && date2 != null && date1 == date2){

                        binding.editTextDates.setText("${formatDate(date1)}")
                        dateUnique = calendar1.timeInMillis
                    }
                }
        }


        binding.buttonExplore.setOnClickListener {
            startExplorerExperienceActivity()
        }


    }


    private fun startExplorerExperienceActivity(){
        val intent = Intent(this, ExplorerExperiencesActivity::class.java)

        if (dateUnique == 0L){
            dateUnique = Calendar.getInstance().timeInMillis
        }

        intent.putExtra(ExplorerExperiencesActivity.TAG20, dateUnique)

        startActivity(intent)
    }


    private fun formatDate(date: Date) : String{
        var simpleDateFormat = SimpleDateFormat("EEE., dd MMM. yyyy")
        return simpleDateFormat.format(date.time)

    }





}
package com.neoland.trimexp.experiences.register


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.neoland.trimexp.databinding.ActivityRegisterExperienceBinding
import java.text.SimpleDateFormat
import java.util.*

class RegisterExperienceActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewIconBack.setOnClickListener {
            goBackRegisterActivity()
        }

        binding.editTextRegisterExpDates.keyListener = null

        binding.editTextRegisterExpDates.setOnClickListener(){

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
                    binding.editTextRegisterExpDates.setText("${formatDate(date1)} - ${formatDate(date2)}")
                } else if (date1 != null && date2 != null && date1 == date2){
                    binding.editTextRegisterExpDates.setText("${formatDate(date1)}")
                }
            }
        }




    }



    private fun goBackRegisterActivity(){
        onBackPressed()
    }

    private fun formatDate(date: Date) : String{
        var simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
        return simpleDateFormat.format(date.time)

    }

}
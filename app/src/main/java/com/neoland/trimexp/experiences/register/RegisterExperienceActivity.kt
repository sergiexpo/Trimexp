package com.neoland.trimexp.experiences.register


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.neoland.trimexp.databinding.ActivityRegisterExperienceBinding
import com.neoland.trimexp.users.register.RegisterUserActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterExperienceActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterExperienceBinding
    lateinit var model: RegisterExperienceViewModel
    private var experiencePhoto : Bitmap? = null
    private var dateFrom: Long = 0
    private var dateTo: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(RegisterExperienceViewModel::class.java)

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
                    dateFrom = calendar1.timeInMillis
                    dateFrom = calendar2.timeInMillis

                } else if (date1 != null && date2 != null && date1 == date2){
                    binding.editTextRegisterExpDates.setText("${formatDate(date1)}")
                    dateFrom = calendar1.timeInMillis
                    dateFrom = calendar2.timeInMillis

                }
            }
        }


        binding.buttonExplorer.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            resultGallery.launch(intent)
        }

        binding.buttonCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultCamera.launch(intent)
        }


        binding.buttonAddExperience.setOnClickListener {
            lifecycleScope.launch{
                model.insertExperience(
                    binding.editTextRegisterExpTitle.text.toString(),
                    binding.editTextRegisterExpDescription.text.toString(),
                    binding.editTextRegisterExpLocation.text.toString(),
                    binding.editTextRegisterExpDuration.text.toString(),
                    binding.editTextRegisterExpPrice.text.toString(),
                    dateFrom,
                    dateTo,
                    2,
                    experiencePhoto
                )
                Toast.makeText(binding.root.context, "Experience has been added", Toast.LENGTH_LONG).show()
                delay(2000)
                finish()
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

    private val resultGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let { uri ->
                contentResolver?.let { contentResolver ->
                    experiencePhoto = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap( this.contentResolver, uri)
                    }
                }
            }
            binding.imageViewRegisterExpPhoto.setImageBitmap(experiencePhoto)
        }

    }

    private val resultCamera  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            experiencePhoto = result.data?.extras?.get("data") as Bitmap
            binding.imageViewRegisterExpPhoto.setImageBitmap(experiencePhoto)
        }
    }



}
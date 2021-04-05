package com.neoland.trimexp.experiences.register


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.neoland.trimexp.BuildConfig
import com.neoland.trimexp.databinding.ActivityRegisterExperienceBinding
import com.neoland.trimexp.home.HomeActivity
import com.neoland.trimexp.places.PlacesAdapter
import com.neoland.trimexp.users.register.RegisterUserActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterExperienceActivity : AppCompatActivity(), PlacesAdapter.OnItemClicked {

    lateinit var binding: ActivityRegisterExperienceBinding
    lateinit var model: RegisterExperienceViewModel
    private var experiencePhoto : Bitmap? = null
    private var dateFrom: Long = 0
    private var dateTo: Long = 0
    private var userId : Int? = null
    private var expLat = 0.0
    private var expLong = 0.0
    private lateinit var geocoder: Geocoder

    private val token = AutocompleteSessionToken.newInstance()
    private lateinit var autocompleteAdapter : PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(RegisterExperienceViewModel::class.java)

        geocoder = Geocoder(this)

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

        autocompleteAdapter = PlacesAdapter(this)
        binding.rvRegisterExperiencePlacesSuggestions.layoutManager = LinearLayoutManager(this)
        binding.rvRegisterExperiencePlacesSuggestions.adapter = autocompleteAdapter
        binding.editTextRegisterExpLocation.addTextChangedListener(watcher)


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

                model.loadPreferences("TAG_EMAIL")?.let{ email ->
                    if (email.isNotEmpty()) {
                        val user = model.getUser(email)
                        userId = user.userId
                    } else {
                        model.loadPreferences("TAG_EMAIL_TEMPORAL")?.let { email_temp ->
                            if (email_temp.isNotEmpty()) {
                                val user = model.getUser(email_temp)
                                userId = user.userId
                            } else {
                                Toast.makeText(binding.root.context, "Please, log in the app", Toast.LENGTH_LONG).show()
                                //Mejor finalizar el fragment y reedirigir
                            }
                        }
                    }
                }

                userId?.let {
                    model.insertExperience(
                            binding.editTextRegisterExpTitle.text.toString(),
                            binding.editTextRegisterExpDescription.text.toString(),
                            getLocality(expLat, expLong),
                            binding.editTextRegisterExpDuration.text.toString().toFloat(),
                            binding.editTextRegisterExpPrice.text.toString().toFloat(),
                            dateFrom,
                            dateTo,
                            it,
                            experiencePhoto
                    )
                }
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

    private fun getLocality(lat: Double, lng: Double): String {
        val list = geocoder.getFromLocation(lat, lng, 1)
        if (list.isEmpty()){
            return  "Locality not found"
        } else {
        return list[0].locality
        }
    }

    private fun getLatitude(address: String) : Double{
        val list = geocoder.getFromLocationName(address, 1)
        if (list.isEmpty()){
            return 0.0
        } else {
            return list[0].latitude
        }
    }

    private fun getLongitude(address: String) : Double{
        //  val geocoder = Geocoder(this)
        val list = geocoder.getFromLocationName(address, 1)
        if (list.isEmpty()){
            return 0.0
        } else {
            return list[0].longitude
        }
    }

    override fun onItemClicked(place: String) {
        binding.editTextRegisterExpLocation.removeTextChangedListener (watcher)
        autocompleteAdapter.updateData(listOf())
        binding.editTextRegisterExpLocation.setText(place)
        expLat = getLatitude(place)
        expLong = getLongitude(place)
        binding.editTextRegisterExpLocation.addTextChangedListener (watcher)
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(textNullable: Editable?) {
            textNullable?.let { text ->
                if (text.count() >= 3) {
                    if (!Places.isInitialized()) {
                        Places.initialize(this@RegisterExperienceActivity, BuildConfig.MAPS_API_KEY, Locale.getDefault())
                    }
                    val placesClient = Places.createClient(this@RegisterExperienceActivity)

                    val request = FindAutocompletePredictionsRequest.builder()
                            .setTypeFilter(TypeFilter.ADDRESS)
                            .setSessionToken(token)
                            .setQuery(text.toString())
                            .build()

                    placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                                val predictionsList = mutableListOf<String>()
                                Log.d(HomeActivity::class.java.name, "Se han recibido las siguientes sugerencias:")
                                for (prediction in response.autocompletePredictions) {
                                    predictionsList.add(prediction.getFullText(null).toString())
                                }
                                autocompleteAdapter.updateData(predictionsList)
                            }.addOnFailureListener { exception: Exception? ->
                                if (exception is ApiException) {
                                    Snackbar.make(binding.root, "Se ha producido un error en la respuesta de Google", Snackbar.LENGTH_LONG).show()
                                }
                            }
                }

            }
        }
    }


}
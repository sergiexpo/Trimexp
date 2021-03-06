package com.neoland.trimexp.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.material.datepicker.MaterialDatePicker
import com.neoland.trimexp.databinding.ActivityHomeBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.material.snackbar.Snackbar
import com.neoland.trimexp.BuildConfig
import com.neoland.trimexp.R
import com.neoland.trimexp.experiences.manage.ManageExperienceActivity
import com.neoland.trimexp.experiences.register.RegisterExperienceActivity
import com.neoland.trimexp.experiences.userlist.UserListExperienceActivity
import com.neoland.trimexp.places.PlacesAdapter
import com.neoland.trimexp.users.favourites.FavouriteUsersListActivity
import com.neoland.trimexp.users.profile.UserProfileActivity
import com.neoland.trimexp.users.register.RegisterUserActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity: AppCompatActivity(), PlacesAdapter.OnItemClicked {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var model: HomeActivityViewModel
    private var dateUnique: Long = 0
    private var currentUserLat : Double? = null
    private var currentUserLong : Double? = null
    private var isUserLogged = false
    private var isUserAlreadyLocated = false
    private lateinit var geocoder: Geocoder

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val token = AutocompleteSessionToken.newInstance()

    private lateinit var autocompleteAdapter : PlacesAdapter

    companion object {
        private const val REQUEST_CODE = 99
    }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this)

        model = ViewModelProvider(this).get(HomeActivityViewModel::class.java)


        model.savePreferences("TAG_EMAIL_TEMPORAL", "")
        model.savePreferences("TAG_PASS_TEMPORAL", "")

        lifecycleScope.launch {
            model.loadPreferences("TAG_EMAIL")?.let {
                if (it.isNotEmpty()) {
                    val user = model.getUser(it)
                    binding.textViewTitle.text = "Welcome ${user.name}"
                    user.mainPhoto?.let{binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setImageResource(it)}
                    user.photoUser?.let{binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
                    binding.navigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.textView_homeMenu_UserName).text = user.name
                    binding.navigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.textView_homeMenu_EmailName).text = user.email
                    isUserLogged = true


                    binding.imageViewIconMenu.setOnClickListener {
                        binding.drawerLayout.openDrawer(Gravity.LEFT)
                    }

                    binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setOnClickListener {
                        startUserProfileActivity(user.userId)
                    }

                } else {
                    binding.textViewTitle.text = "Welcome Visitor"
                    binding.imageViewIconMenu.setOnClickListener {
                        showDialogLogIn()
                    }
                }
            }
        }

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())

        // val tvWelcome = binding.navigationViewRight.getHeaderView(0).findViewById<ImageView>(R.id.imageView_photosample)


        binding.navigationViewHome.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menuHome_addExperience -> {
                    startRegisterExperienceActivity()
                }
                R.id.menuHome_listExperiencesUser ->{
                    startUserListExperienceActivity()
                }
                R.id.menuHome_manageExperiencesUser->{
                    startManageExperienceActivity()
                }
                R.id.menuHome_favouritesUsers->{
                    startFavouriteUsersListActivity()
                }

                R.id.menuHome_LogOut->{
                    showDialogLogOut()
                }

                else -> {
                    return@setNavigationItemSelectedListener false
                }
            }

            binding.drawerLayout.closeDrawers()
            true
        }



        binding.editTextDates.keyListener = null
        binding.editTextLogIn.keyListener = null

        checkCurrentUserLocation()
        checkDate()


        autocompleteAdapter = PlacesAdapter(this)
        binding.rvPlacesSuggestions.layoutManager = LinearLayoutManager(this)
        binding.rvPlacesSuggestions.adapter = autocompleteAdapter
        binding.editTextLocation.addTextChangedListener(watcher)




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


        binding.imageViewButtonExplore.setOnClickListener {
            if (model.status.value.isNullOrEmpty()){
                model.status.observe(this){
                    startExplorerExperienceActivity()
                }
            } else {
            startExplorerExperienceActivity()
            }
        }





        binding.editTextLogIn.setOnClickListener {
            showDialogLogIn()
        }



    }


    override fun onItemClicked(place: String) {
        binding.editTextLocation.removeTextChangedListener (watcher)
        autocompleteAdapter.updateData(listOf())
        binding.editTextLocation.setText(place)
        currentUserLat = getLatitude(place)
        currentUserLong = getLongitude(place)
        binding.editTextLocation.addTextChangedListener (watcher)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                checkCurrentUserLocation()
            } else {
                binding.editTextLocation.setText("Location can not be found")
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkCurrentUserLocation() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            fusedLocationClient.lastLocation.addOnSuccessListener {

                if (it == null){
                    currentUserLat = 0.0
                    currentUserLong = 0.0
                } else {
                    currentUserLat = it.latitude
                    currentUserLong = it.longitude
                }

                currentUserLat?.let{ latitude ->
                    currentUserLong?.let { longitude ->
                        val address = getAddress(latitude,longitude)
                        binding.editTextLocation.setText(address)
                        isUserAlreadyLocated = true
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }



    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(textNullable: Editable?) {
            if (isUserAlreadyLocated == true) {
                textNullable?.let { text ->
                    Log.d(HomeActivity::class.java.name, "El usuario ha escrito $text")
                    if (text.count() >= 3) {
                        Log.d(HomeActivity::class.java.name, "Comenzamos a filtrar $text")
                        if (!Places.isInitialized()) {
                            Places.initialize(this@HomeActivity, BuildConfig.MAPS_API_KEY, Locale.getDefault())
                        }

                        val placesClient = Places.createClient(this@HomeActivity)

                        val request = FindAutocompletePredictionsRequest.builder()
                                //      .setOrigin(location)
                                //      .setCountries("ES")
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

                                        Log.d(HomeActivity::class.java.name, "${prediction.getFullText(null)}")
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


    private fun checkDate() {
        binding.editTextDates.setText("${formatDate(Date())}")
        dateUnique = Calendar.getInstance().timeInMillis
    }


    private fun startExplorerExperienceActivity() {
        val intent = Intent(this, ExplorerExperiencesActivity::class.java)

        /*      if (dateUnique == 0L){
              dateUnique = Calendar.getInstance().timeInMillis
          } */

        /*    if(lat == 0.0 || long == 0.0){
            checkLocation()    //Hacer algo para que espere
        } */

        intent.putExtra(ExplorerExperiencesActivity.TAG31, currentUserLat)
        intent.putExtra(ExplorerExperiencesActivity.TAG32, currentUserLong)
        intent.putExtra(ExplorerExperiencesActivity.TAG30, dateUnique)

        startActivity(intent)
    }


    private fun formatDate(date: Date): String {
        var simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
        return simpleDateFormat.format(date.time)

    }

    private fun getAddress(lat: Double, lng: Double): String {
        val list = geocoder.getFromLocation(lat, lng, 1)
        if (list.isEmpty()){
            return "Address not found"
        } else {
            return list[0].locality + ", " + list[0].postalCode + ", " + list[0].countryName
        }
    }

    private fun getLatitude(address: String) : Double?{
        val list = geocoder.getFromLocationName(address, 1)
        if (list.isEmpty()){
            return null
        } else {
            return list[0].latitude
        }
    }

    private fun getLongitude(address: String) : Double?{
        //  val geocoder = Geocoder(this)
        val list = geocoder.getFromLocationName(address, 1)
        if (list.isEmpty()){
            return null
        } else {
            return list[0].longitude
        }
    }


    // MARK - Start Activities

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun startRegisterUserActivity() {
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
    }

    private fun startRegisterExperienceActivity() {
        val intent = Intent(this, RegisterExperienceActivity::class.java)
        startActivity(intent)
    }

    private fun startUserListExperienceActivity() {
        val intent = Intent(this, UserListExperienceActivity::class.java)
        startActivity(intent)
    }

    private fun startManageExperienceActivity() {
        val intent = Intent(this, ManageExperienceActivity::class.java)
        startActivity(intent)
    }

    private fun startFavouriteUsersListActivity() {
        val intent = Intent(this, FavouriteUsersListActivity::class.java)
        startActivity(intent)
    }

    private fun startUserProfileActivity(userId: Int){
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(UserProfileActivity.TAG21, userId)
        startActivity(intent)
    }


    // MARK - Dialogs

    private fun showDialogLogIn() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //dialog.setCancelable(true)
        dialog.setContentView(R.layout.options_menu_login)


        val editTextEmail = dialog.findViewById(R.id.editText_email) as EditText
        val editTextPassword = dialog.findViewById(R.id.editText_Password) as EditText
        val checkBox = dialog.findViewById(R.id.checkBox_rememberUser) as CheckBox
        val buttonSignIn = dialog.findViewById(R.id.button_signIn) as Button
        val buttonLogIn = dialog.findViewById(R.id.button_logIn) as Button
        val buttonCancel = dialog.findViewById(R.id.imageView_iconCancel) as ImageView


        model.loadPreferences("TAG_EMAIL")?.let {
            checkBox.isChecked = it.isNotEmpty()
            editTextEmail.setText(it)
        }

        model.loadPreferences("TAG_PASS")?.let {
            checkBox.isChecked = it.isNotEmpty()
            editTextPassword.setText(it)
        }


        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSignIn.setOnClickListener {
            startRegisterUserActivity()
        }

        buttonLogIn.setOnClickListener {

            lifecycleScope.launch {
                if (model.existsUser(editTextEmail.text.toString(), editTextPassword.text.toString())) {

                    if (checkBox.isEnabled && checkBox.isChecked) {
                        model.savePreferences("TAG_EMAIL", editTextEmail.text.toString())
                        model.savePreferences("TAG_PASS", editTextPassword.text.toString())
                    } else {
                        model.savePreferences("TAG_EMAIL", "")
                        model.savePreferences("TAG_PASS", "")
                        model.savePreferences("TAG_EMAIL_TEMPORAL", editTextEmail.text.toString())
                        model.savePreferences("TAG_PASS_TEMPORAL", editTextPassword.text.toString())
                    }

                    Toast.makeText(binding.root.context, "Welcome", Toast.LENGTH_LONG).show()
                    dialog.dismiss()

                    val user = model.getUser(editTextEmail.text.toString())
                    binding.textViewTitle.text = "Welcome ${user.name}"
                    user.mainPhoto?.let{binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setImageResource(it)}
                    user.photoUser?.let{binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
                    binding.navigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.textView_homeMenu_UserName).text = user.name
                    binding.navigationViewHome.getHeaderView(0).findViewById<TextView>(R.id.textView_homeMenu_EmailName).text = user.email

                    isUserLogged = true
                    binding.imageViewIconMenu.setOnClickListener {
                        binding.drawerLayout.openDrawer(Gravity.LEFT)
                    }

                    binding.navigationViewHome.getHeaderView(0).findViewById<CircleImageView>(R.id.imageView_homeMenu_PhotoUser).setOnClickListener {
                        startUserProfileActivity(user.userId)
                    }

                } else {
                    Toast.makeText(binding.root.context, "User or password incorrect", Toast.LENGTH_LONG).show()
                }
            }
        }
        dialog.show()
    }


    private fun showDialogLogOut(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.options_menu_logout)


        val imageIconCancel = dialog.findViewById(R.id.imageView_iconCancel) as ImageView
        val buttonLogOut = dialog.findViewById(R.id.button_logout) as Button

        imageIconCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonLogOut.setOnClickListener {
            model.savePreferences("TAG_EMAIL", "")
            model.savePreferences("TAG_PASS", "")
            model.savePreferences("TAG_EMAIL_TEMPORAL", "")
            model.savePreferences("TAG_PASS_TEMPORAL", "")
            Toast.makeText(binding.root.context, "Succesfully log out", Toast.LENGTH_LONG).show()
            finish()
            startHomeActivity()

        }
        dialog.show()
    }


}
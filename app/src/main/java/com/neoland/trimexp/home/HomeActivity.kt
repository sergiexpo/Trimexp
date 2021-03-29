package com.neoland.trimexp.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.neoland.trimexp.databinding.ActivityHomeBinding
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.neoland.trimexp.R
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesFragment
import com.neoland.trimexp.experiences.register.RegisterExperienceActivity
import com.neoland.trimexp.users.register.RegisterUserActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity: AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var model: HomeActivityViewModel
    private var dateUnique: Long = 0
    private var currentUserLat = 0.0
    private var currentUserLong = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val REQUEST_CODE = 99
    }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        model = ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        lifecycleScope.launch {
            model.loadPreferences("TAG_EMAIL")?.let {
                if (it.isNotEmpty()) {
                    val user = model.getUser(it)
                    binding.textViewTitle.text = "Welcome ${user.name}"
                } else {
                    binding.textViewTitle.text = "Welcome Visitor"
                }
            }
        }

        binding.imageViewLogAct.setImageResource(model.getLoginActivityImage())

        binding.imageViewIconMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.navigationViewHome.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome_addExperience -> {
                    startRegisterExperienceActivity()
                }
                else -> {
                    return@setNavigationItemSelectedListener false
                }
            }

            binding.drawerLayout.closeDrawers()
            true
        }




        binding.editTextDates.keyListener = null
        binding.editTextSignIn.keyListener = null
        binding.editTextLogIn.keyListener = null

        checkCurrentUserLocation()
        checkDate()

        binding.editTextLocation.setOnClickListener {

            //AÑADIR UNA CIUDAD DE UNA LISTA EXISTENTE, y calcular coordenadas

        }

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

        binding.editTextSignIn.setOnClickListener {
            startRegisterUserActivity()
        }

        binding.editTextLogIn.setOnClickListener {
            showDialogLogIn()
        }



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

                currentUserLat = it.latitude
                currentUserLong = it.longitude
                val adress = getAddress(currentUserLat,currentUserLong)
                binding.editTextLocation.setText(adress)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }

    private fun checkDate(){
        binding.editTextDates.setText("${formatDate(Date())}")
        dateUnique = Calendar.getInstance().timeInMillis
    }


    private fun startExplorerExperienceActivity(){
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


    private fun formatDate(date: Date) : String{
        var simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
        return simpleDateFormat.format(date.time)

    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list[0].locality + ", " + list[0].postalCode + ", " + list[0].countryName
    }


    private fun startRegisterUserActivity(){
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
    }

    private fun startRegisterExperienceActivity(){
        val intent = Intent(this, RegisterExperienceActivity::class.java)
        startActivity(intent)
    }

    private fun showDialogLogIn() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //dialog.setCancelable(true)
        dialog.setContentView(R.layout.options_menu_login)


        val editTextEmail = dialog.findViewById(R.id.editText_email) as EditText
        val editTextPassword = dialog.findViewById(R.id.editText_Password) as EditText
        val checkBox = dialog.findViewById(R.id.checkBox_rememberUser) as CheckBox
        val buttonCancel = dialog.findViewById(R.id.button_cancel) as Button
        val buttonLogIn = dialog.findViewById(R.id.button_logIn) as Button


        model.loadPreferences("TAG_EMAIL")?.let{
            checkBox.isChecked = it.isNotEmpty()
           editTextEmail.setText(it)
        }

        model.loadPreferences("TAG_PASS")?.let{
            checkBox.isChecked = it.isNotEmpty()
            editTextPassword.setText(it)
        }


        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonLogIn.setOnClickListener {

            lifecycleScope.launch {
                if (model.existsUser(editTextEmail.text.toString(), editTextPassword.text.toString())) {

                    if (checkBox.isEnabled && checkBox.isChecked){
                        model.savePreferences("TAG_EMAIL", editTextEmail.text.toString())
                        model.savePreferences("TAG_PASS", editTextPassword.text.toString())
                    } else {
                        model.savePreferences("TAG_EMAIL", "")
                        model.savePreferences("TAG_PASS", "")
                    }

                    Toast.makeText(binding.root.context, "Welcome", Toast.LENGTH_LONG).show()
                    dialog.dismiss()

                    val user =  model.getUser(editTextEmail.text.toString())
                    binding.textViewTitle.text = "Welcome ${user.name}"

                } else {
                    Toast.makeText(binding.root.context, "User or password incorrect", Toast.LENGTH_LONG).show()
                }
            }
        }
        dialog.show()
    }




    }
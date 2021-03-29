package com.neoland.trimexp.experiences.detail

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.ActivityDetailExperienceBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG20
import com.neoland.trimexp.experiences.explorer.ExplorerExperiencesActivity.Companion.TAG10
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExperienceDetailActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityDetailExperienceBinding
    private lateinit var model: ExperienceDetailViewModel
    private lateinit var experience: Experience

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExperienceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)

        binding.imageViewIconBack.setOnClickListener {
            goBackExplorerActivity()
        }


        lifecycleScope.launch(Dispatchers.Main) {

            experience = model.getExperience(intent.getIntExtra(TAG10, 0))

            binding.imageViewMainPhoto.setImageResource(experience.mainPhoto)
            binding.textViewTitleDetailExp.text = experience.title
            binding.textViewDescriptionDetailExp.text = experience.description
            binding.textViewDateDetailExp.text = experience.dateFrom.toString()
            binding.textViewDurationDetailExp.text = experience.duration
            binding.textViewPriceDetailExp .text = experience.price + " " + experience.divisa
            binding.textViewAdressDetailExp .text = experience.adress

            val user =  model.getUser(intent.getIntExtra(TAG20, 0))
            binding.textViewUserName.text = user.name
            user.mainPhoto?.let{ binding.imageViewPhotoUser.setImageResource(it)}
            user.photoUser?.let{binding.imageViewMainPhoto.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}

            val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
            mapFragment.getMapAsync(this@ExperienceDetailActivity)
        }


    }



    private fun goBackExplorerActivity(){
        onBackPressed()
    }

    override fun onMapReady(googleMap: GoogleMap) {

         var lat = experience.latitud
         var long = experience.longitud

            val location = LatLng(lat, long)
            googleMap.addMarker(MarkerOptions()
                .position(location)
                .title("${experience.title}")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pin_experiences))
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))

    }

}
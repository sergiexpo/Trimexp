package com.neoland.trimexp.experiences.explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.FragmentExperiencesMapBinding
import com.neoland.trimexp.databinding.FragmentExperienceslistBinding
import kotlinx.coroutines.launch

class ExplorerExperiencesMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentExperiencesMapBinding
    private lateinit var model : ExplorerExperiencesFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(ExplorerExperiencesFragmentViewModel::class.java)

        model.getAllExperiences() // TEMPORAL

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperiencesMapBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_experiencesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        model.experiences.value?.forEach {
            val location = LatLng(it.latitud, it.longitud)
            googleMap.addMarker(MarkerOptions().position(location).title("${it.title}"))
        }
     /*   googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMadrid)) */
    }



}
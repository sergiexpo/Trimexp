package com.neoland.trimexp.experiences.explorer

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.neoland.trimexp.R
import com.neoland.trimexp.databinding.FragmentExperiencesMapBinding
import com.neoland.trimexp.databinding.FragmentExperienceslistBinding
import com.neoland.trimexp.entities.Experience
import com.neoland.trimexp.experiences.detail.ExperienceDetailActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.absoluteValue

class ExplorerExperiencesMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentExperiencesMapBinding
    private lateinit var model : ExplorerExperiencesFragmentViewModel
    private var date : Long = 0
    private var lat = 0.0
    private var long = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(ExplorerExperiencesFragmentViewModel::class.java)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperiencesMapBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = arguments?.getLong("LONG") ?: 0L
        lat = arguments?.getDouble("LATITUD") ?: 0.0
        long = arguments?.getDouble("LONGITUD") ?: 0.0

        model.getExplorerExperiencesList(lat, long, date)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_experiencesMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        var userCurrentLocation = LatLng(lat, long)

        model.experiences.observe(viewLifecycleOwner) {

            model.experiences.value?.forEach {
                val location = LatLng(it.latitud, it.longitud)
                googleMap.addMarker(MarkerOptions()
                        .position(location)
                        .title("${it.title}")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pin_experiences))
                        .snippet("${it.id}")
                )
            }
        }

        googleMap.addMarker(MarkerOptions()
            .position(userCurrentLocation)
            .title("Current User Location")
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pin_user))
            )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 15F))


        googleMap.setOnMarkerClickListener {

            if (it.snippet == null){
                Toast.makeText(binding.root.context, "${it.title}" , Toast.LENGTH_LONG).show()
            } else {
           // Toast.makeText(binding.root.context, "${it.title}  ||| ${it.snippet}", Toast.LENGTH_LONG).show()
                showDialogInfoMarkerMap(it.snippet.toInt())
            }
        true
        }


    }

    private fun showDialogInfoMarkerMap(id : Int){
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.options_menu_marker_map)

        val photo = dialog.findViewById(R.id.imageView_markerMap_photoExp) as ImageView
        val title = dialog.findViewById(R.id.textView_markerMap_titleExp) as TextView
        val ratingBar = dialog.findViewById(R.id.ratingBar_markerMap_experience) as RatingBar
        val imageDistance = dialog.findViewById(R.id.imageView_markerMap_location) as ImageView
        val distanceText = dialog.findViewById(R.id.textView_markerMap_location) as TextView
        val duration = dialog.findViewById(R.id.textView_markerMap_duration) as TextView
        val imagePrice = dialog.findViewById(R.id.imageView_markerMap_Price) as ImageView
        val price = dialog.findViewById(R.id.textView_markerMap_Price) as TextView
        val buttonCancel = dialog.findViewById(R.id.button_markerMap_cancel) as Button
        val buttonDetail = dialog.findViewById(R.id.button_markerMap_detail) as Button

        lifecycleScope.launch(Dispatchers.Main) {

            var experience = model.getExperience(id)

            var distance = experience.getDistanceFromUserCurrenLocation(lat, long)

            experience.mainPhoto?.let{photo.setImageResource(it)}
            experience.photoExperience?.let{photo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0 , it.size))}
            title.text = experience.title
            ratingBar.rating = experience.ratingValoration
            imageDistance.setColorFilter(Color.parseColor(colorDistance(distance)))
            distanceText.setTextColor(Color.parseColor(colorDistance(distance)))
            distanceText.text = distanceInKm(distance) + " ${unitDistance(distance)}"
            duration.text = formatDuration(experience.duration)
            imagePrice.setColorFilter(Color.parseColor(colorPrice(experience.price)))
            price.setTextColor(Color.parseColor(colorPrice(experience.price)))
            price.text = formatIsFree(experience.price,experience.currency)

            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }

            buttonDetail.setOnClickListener {
                startExperienceDetailActivity(experience)
            }
            dialog.show()
        }
    }

    private fun startExperienceDetailActivity(experience: Experience){

        activity?.let{
            val intent = Intent(it, ExperienceDetailActivity::class.java)

            intent.putExtra(ExplorerExperiencesActivity.TAG10, experience.id)
            intent.putExtra(ExplorerExperiencesActivity.TAG20, experience.fkUserIdOwner)

            it.startActivity(intent)
        }

    }



    fun formatDuration(duration: Float) : String{

        var formatDistanceM = DecimalFormat("#")

        if (duration.absoluteValue % 1.0 >= 0.005) {
            return duration.toString() + " hours"
        } else {
            return formatDistanceM.format(duration).toString() + " hours"
        }
    }


    // MARK - Format Functions Price

    fun colorPrice(price: Float) : String  {
        if (price == 0F) {
            return "#688db9"
        } else {
            return "#ff0000"
        }
    }

    fun formatIsFree(price: Float, currency: String) : String{

        var formatDistanceM = DecimalFormat("#")

        if (price == 0F) {
            return "Free"
        } else {
            if (price.absoluteValue % 1.0 >= 0.005) {
                return price.toString() + "$currency"
            } else {
                return formatDistanceM.format(price).toString() + "$currency"
            }
        }
    }


    // MARK - Format Functions Distance

    fun colorDistance(distance: Float) : String {
        if (distance < 3000F) {
            return "#419F00"
        } else{
            return  "#EC8800"
        }
    }

    fun distanceInKm(distance: Float) : String {

        var formatDistanceM = DecimalFormat("#")
        var formatDistanceKm = DecimalFormat("#.00")

        if (distance < 1000F) {
            return formatDistanceM.format(distance).toString()
        } else {
            return formatDistanceKm.format(distance / 1000).toString()
        }
    }

    fun unitDistance(distance: Float) : String {
        if (distance < 1000F) {
            return "m"
        } else{
            return  "Km"
        }
    }

}
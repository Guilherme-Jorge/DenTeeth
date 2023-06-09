package br.edu.puccampinas.denteeth.emergencia

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.edu.puccampinas.denteeth.R
import br.edu.puccampinas.denteeth.TelaPrincipalActivity
import br.edu.puccampinas.denteeth.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val permissionCode = 101
    private lateinit var binding: ActivityMapsBinding
    lateinit var lat: String
    lateinit var lng: String
    lateinit var titulo: String
    lateinit var endereco: String
    var latsocorrista: Double = 0.0
    var lngsocorrista: Double = 0.0

    var locationsocorrista = LatLng (latsocorrista, lngsocorrista)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        buscarlocation()

        binding.btnmapout.setOnClickListener {
            val intentMapout = Intent(binding.root.context, TelaPrincipalActivity::class.java)
            intentMapout.putExtra("fcmToken", intent.getStringExtra("fcmToken"))
            this.startActivity(intentMapout)
        }
    }

    private fun buscarlocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }

        val task = fusedLocationProvider.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                Toast.makeText(this, currentLocation.latitude.toString() + "" + currentLocation.longitude.toString(), Toast.LENGTH_SHORT).show()

                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)

        lat = intent.getStringExtra("lat")!!
        lng = intent.getStringExtra("lng")!!
        titulo = intent.getStringExtra("titulo")!!
        endereco = intent.getStringExtra("endereco")!!

        latsocorrista = lat.toDouble()
        lngsocorrista = lng.toDouble()

        val makerOptions = MarkerOptions().position(latLng).title("Você está aqui")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(makerOptions)
        googleMap.addMarker(
            MarkerOptions()
                .position(locationsocorrista)
                .title("Título: ${titulo}")
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode -> if (grantResults.isEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                buscarlocation()
            }
        }
    }
}
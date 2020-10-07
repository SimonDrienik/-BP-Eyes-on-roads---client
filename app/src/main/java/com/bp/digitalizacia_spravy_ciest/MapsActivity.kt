package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity :AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    val id = "1"
    val date = "7.10.2020"
    val kategoria = "strom na ceste"
    val popis = "text popisu"
    val stav_problemu = "nevyriesene"
    val stav_riesenia = "v procese"
    val popis_riesenia_problemu = "nechce sa nam"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val Trnava = LatLng(48.380624, 17.580751)
        map.addMarker(MarkerOptions().position(Trnava).title("Vitajte v meste Trnava"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Trnava, 12.0f)) //nastavenie inintial zoom
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        setUpMap()
        setMapLongClick(map)
        map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this)) //pridanie noveho layoutu pre info window
       // setPoiClick(map)
    }
    //pridanie zoomovania na mapke po kliknuti na marker
    class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(p0: Marker?) = false
        override fun onMapReady(p0: GoogleMap?) {
            TODO("Not yet implemented")
        }


    }

    override fun onMarkerClick(p0: Marker?) = false

    //nastavenie vyzadovanie povolenia pre zdielanie polohy
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    private fun setUpMap() {
        //zistime ci appka ziskala povolenie na zdielanie uzivatelovej polohy a ak nie tak poziada
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //umoznuje aby poloha bola na mape zvyraznena modrym kruhom, na ktory ked sa klikne sa vycentruje pozicia
        map.isMyLocationEnabled = true
        //najaktualnejsiu dostupnu polohu udava:
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            //ak sa podarila ziskat najaktualnejsia poloha tak screen presmeruje na polohu
            if (location != null){
                lastLocation = location //posledna ziskana poloha
                val currentLatLng = LatLng(location.latitude, location.longitude) //ziskanie surandic aktualnej polohy
               // placeMarkerOnMap(currentLatLng) //zobrazenie markera
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f)) //nastavenie screenu na poziciu a zoom
            }
        }
    }

    //pridanie uzivatelovu poziciu ako marker na mapu
    private fun placeMarkerOnMap(location: LatLng) {
        //vytvorime MarkerOptions objekt a nastavime aktualnu polohu ako poziciu pre marker
        val markerOptions = MarkerOptions().position(location)
        //prida marker na mapu
        map.addMarker(markerOptions)
    }

    //pridanie markera po dlhom stlaceni screenu
    private fun setMapLongClick(map:GoogleMap) {
        map.setOnMapLongClickListener {latLng ->

            //info okno o markeru
           val snippet = String.format(
                Locale.getDefault(),
                "Position: Lat: %1$.5f, Long: %2$.5f\n " +
                        "id: $id\n " +
                        "datum: $date\n " +
                        "kategoria: $kategoria\n " +
                        "popis: $popis\n " +
                        "stav problemu: $stav_problemu\n " +
                        "stav riesenia problemu: $stav_riesenia\n " +
                        "popis rieseneho problemu: $popis_riesenia_problemu\n ",
                latLng.latitude,
                latLng.longitude
            )

            map.addMarker(
                MarkerOptions()
                    .title("cestny problem")
                    .position(latLng)
                    .snippet(snippet)
            )
            val intent = Intent(this, NewReportActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
    //kliknutie na nejaky bojekt ako obchod, restauracia.... vyznaci marker s nazvom daneho objektu5y5y5y5y
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            //zoberie nazov poi objektu
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()

        }
    }


}



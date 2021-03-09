package com.bp.digitalizacia_spravy_ciest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.ShowAllProblemsData
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.map_activity.*
import kotlinx.android.synthetic.main.problems_list.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDateTime
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class MapsActivity :AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {
    //map stuff
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

//  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    //problem info stuff
    private var id : Int = 0
    private lateinit var file: File
    private var stav_vozovky : String = ""
    private var stav_problemu : String = ""
    private var stav_riesenia_problemu : String = ""
    private var description : String = ""
    private var popis_stavu_riesenia_problemu : String = ""
    var extras : Bundle? = null
    var i : Int = 0
    private  var imgId : Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
     val selectedCurrent: LocalDateTime = LocalDateTime.now()

    // override the onSupportNavigateUp() function to launch the Drawer when the hamburger icon is clicked


    // override the onBackPressed() function to close the Drawer when the back button is clicked
    /*override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            this.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }*/

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)

        /////////////////////////////MENU STUFF/////////////////////////////////
        //hamburger for side menu drawer
        val hamburger = findViewById<ImageView>(R.id.imageView)
        hamburger?.setOnClickListener()
        {
            this.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()

        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuNastavenia -> {
                    Toast.makeText(this, "nastavenia", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menuMojeProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    startActivity(intent2)
                    true
                }
                R.id.menuVsetkyProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    intent2.putExtra("pocet", i)
                    startActivity(intent2)
                    true
                }
                R.id.menuZoznamPouzivatelov -> {
                    Toast.makeText(this, "zoznam pouzivatelov", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.mapFragment4 -> {
                    Intent(this, com.bp.digitalizacia_spravy_ciest.ui.MapsActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }
                R.id.menuPrihlasenie -> {
                    Intent(this, LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
        ///////////////END OF MENU STUFF////////////////////////////////////////////

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d("TAG", "test1")
        extras = intent.extras
        if (null != extras) {
            imgId = extras!!.getInt("imgID")
            stav_vozovky = extras!!.getString("stav_vozovky").toString()
            stav_problemu = extras!!.getString("stav_problemu").toString()
            //stav_riesenia_problemu = extras.getString("stav_riesenia_problemu").toString()
            description = extras!!.getString("description").toString()
            //popis_stavu_riesenia_problemu = extras.getString("popis_stavu_riesenia_problemu").toString()
        }

        val buttonNewReport = findViewById<ImageView>(R.id.imageView3)
        buttonNewReport?.setOnClickListener()
        {
            Intent(this, NewReportActivity::class.java).apply {
                startActivity(this)
            }
        }
        /*map.setOnInfoWindowClickListener(object: GoogleMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(marker:Marker) {
            }
        })*/

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
        map.getUiSettings().setZoomControlsEnabled(false)
        map.setOnMarkerClickListener(this)
        setUpMap()
        getAll(map)
        if (extras != null) {
            setMapLongClick(map)
        }
        map.setOnInfoWindowClickListener(this)
        map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this)) //pridanie noveho layoutu pre info window
       // setPoiClick(map)


    }
    override fun onInfoWindowClick(marker: Marker) {
        val pozicia = marker.position
        Toast.makeText(
            this, "Info window clicked, $pozicia",
            Toast.LENGTH_SHORT
        ).show()
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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
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
    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            //info okno o markeru
            Locale.getDefault()
            val lat = latLng.latitude.toString().dropLast(8)
            val lng = latLng.longitude.toString().dropLast(8)
            val poloha = lat + "," + lng

            val request = ServiceBuilder.buildService(CallsAPI::class.java)

            val jsonObject = JSONObject()
            jsonObject.put("poloha", poloha)
            jsonObject.put("popis_problemu", description)
            jsonObject.put("kategoria_problemu", stav_vozovky)
            jsonObject.put("stav_problemu", stav_problemu)
            jsonObject.put("imgID", imgId)

            val jsonObjectString = jsonObject.toString()

            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val call = request.addProblem1(poloha, description, stav_vozovky, stav_problemu, imgId)
            Log.d("TAGGGGGGGG", poloha)
            Log.d("TAGgggggggg", description)
            Log.d("TAG", stav_vozovky)
            Log.d("TAG", stav_problemu)
            Log.d("TAG", imgId.toString())

            call.enqueue(object : Callback<Int> {
                override fun onResponse(
                    call: Call<Int>,
                    response: Response<Int>
                ) {
                    if (response.body() == 1) {
                        finish();
                        startActivity(this@MapsActivity.intent);
                        Toast.makeText(
                            this@MapsActivity,
                            "zaznam vytvoreny",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        Toast.makeText(
                            this@MapsActivity,
                            "NEJDZE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


            /*CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response

                Log.e("INFO O PROBLEME", poloha)
                Log.e("INFO O PROBLEME", description)
                Log.e("INFO O PROBLEME", stav_vozovky)
                Log.e("INFO O PROBLEME", stav_problemu)
                Log.e("INFO O PROBLEME", imgId.toString())
                withContext(Dispatchers.Main) {
                    val response = request.addProblem1(
                        poloha = poloha,
                        popis_problemu = description,
                        kategoria_problemu = stav_vozovky,
                        stav_problemu = stav_problemu,
                        imgId = imgId
                    )
                    if (response.isSuccessful) {

                        finish();
                        startActivity(this@MapsActivity.intent);
                        Toast.makeText(
                            this@MapsActivity,
                            "Zaznam uspesne vytvoreny",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Log.e("RETROFIT_ERROR", response.code().toString())
                        Toast.makeText(
                            this@MapsActivity,
                            response.errorBody().toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }*/



        }
    }

    fun getAll(map: GoogleMap) {
        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems()
        call!!.enqueue(object : Callback<List<ShowAllProblemsData?>?> {
            override fun onResponse(
                call: Call<List<ShowAllProblemsData?>?>,
                response: Response<List<ShowAllProblemsData?>?>
            ) {

                val problemList = response.body()
                Log.d("TAG", "vypis")
                //Log.i("TAG", problemList!![0]!!.poloha+"");
                if (problemList != null) {
                    i = 0

                    for (item in problemList) {

                        var pos = item!!.position
                        var postSplit = pos?.split(",")
                        var pos1 = postSplit?.get(0)?.toDouble()
                        var pos2 = postSplit?.get(1)?.toDouble()
                        var id_problemu = item!!.id
                        var popis = item!!.popis
                        var kategoria = item!!.kategoria
                        var stavRieseniaProblemu = item!!.stav_riesenia_problemu
                        var stav_problemu = item!!.stav_problemu
                        var PopisStavuRieseniaProblemu = item!!.popis_riesenia_problemu
                        var created_at = item!!.created_at

                        // Toast.makeText(this@MapsActivity, "ok $i", Toast.LENGTH_SHORT).show()

                        Log.d("TAG", pos1.toString())
                        Log.d("TAG", pos2.toString())
                        Log.d("TAG", id_problemu.toString())
                        Log.d("TAG", popis.toString())
                        val snippet = String.format(
                            Locale.getDefault(),
                                    " kategoria: $kategoria\n " +
                                    "Stav Riesenia Problemu: $stavRieseniaProblemu\n " +
                                    "Stav Problemu: $stav_problemu\n " +
                                    "datum: $created_at"

                        )

                        map.addMarker(
                            MarkerOptions()
                                .title("cestny problem")
                                .position(LatLng(pos1!!, pos2!!))
                                .snippet(snippet)

                        )

                        i += 1
                    }
                }
            }

            override fun onFailure(call: Call<List<ShowAllProblemsData?>?>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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






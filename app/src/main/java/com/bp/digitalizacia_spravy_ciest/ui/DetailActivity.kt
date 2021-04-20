package com.bp.digitalizacia_spravy_ciest.ui

import android.R.layout.simple_spinner_item
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.*
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_comment_activity.*
import kotlinx.android.synthetic.main.custom_list.*
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import kotlinx.android.synthetic.main.login_activity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {
    //choose img from lib
    private val WRITE_REQUEST_CODE = 101
    private val READ_REQUEST_CODE = 101

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_REQUEST_CODE)
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {

                }
            }
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {

                }
            }
        }
    }


    companion object {
        private const val READ_EXTERNAL_STORAGE = 1
    }



    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    var extras : Bundle? = null
    private var idProblem : Int = 0
    var loged = 0
    private var from : Int = 0

    private var datum : String = ""
    private var vytvoril : String = ""
    private var popis : String = ""
    private var zamestnanec : String = ""
    private var priorita : String = ""
    private var poloha : String = ""
    private var kategoria : String = ""
    private var stavProblemu : String = ""
    private var stavRiesenia : String = ""
    private var vozidlo : String = ""
    private var popisRiesenia : String = ""

    private var imgProblemUrl : String = ""
    private var imgRiesenieUrl : String = ""

    private lateinit var zamestnanci : List<String>
    private lateinit var priority : List<String>
    private lateinit var kategorie : List<String>
    private lateinit var stavyProblemu : List<String>
    private lateinit var stavyRiesenia : List<String>
    private lateinit var vozidla : List<String>

    lateinit var spinnerZamestnanec: Spinner
    lateinit var spinnerPriorita: Spinner
    lateinit var spinnerKategoria: Spinner
    lateinit var spinnerStavProblemu: Spinner
    lateinit var spinnerVozidlo: Spinner
    lateinit var spinnerStavRieseniaProblemu: Spinner

    //img
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var imageView: ImageView
    lateinit var file: File

    private var zamestnanecChange = ""
    private var prioritaChange = ""
    private var kategoriaChange = ""
    private var stavChange = ""
    private var stavRieseniaChange = ""
    private var priradeneVozidloChange = ""
    private var opisRieseniaChange: String = ""

    lateinit var verejne: CheckBox


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)




        /////////////////////////////MENU STUFF/////////////////////////////////
        //hamburger for side menu drawer
        val hamburger = findViewById<ImageView>(R.id.imageViewLP)
        hamburger?.setOnClickListener()
        {
            this.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayoutLP)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()

        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navViewLP)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuNastavenia -> {
                    Toast.makeText(this, "nastavenia", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menuMojeProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    intent2.putExtra("allProblems", 0)
                    startActivity(intent2)
                    true
                }
                R.id.menuVsetkyProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    intent2.putExtra("allProblems", 1)
                    startActivity(intent2)
                    true
                }
                R.id.menuZoznamPouzivatelov -> {
                    val intent2 = Intent(this, UsersListActivity::class.java)
                    startActivity(intent2)
                    true
                }
                R.id.mapFragment4 -> {
                    Intent(this, MapsActivity::class.java).apply {
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
                R.id.menuRegistracia -> {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://147.175.204.24/register"))
                    startActivity(i)
                    true
                }
                R.id.menuOdhlasenie -> {
                    sessionManager.clear()
                    Intent(this, MapsActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        sessionManager = SessionManager(this)
        //Actual user info
        val header = navView.getHeaderView(0)
        header.userName.text = sessionManager.fetchUserName().toString()

        //menu for unregistered user, role_id == 2
        if (sessionManager.fetchUserId() == null) {
            header.prihlaseny.visibility = View.GONE
            navView.menu.getItem(0).isVisible = false
            navView.menu.getItem(1).isVisible = false
            navView.menu.getItem(3).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(1).isVisible = false
        }

        //menu for registred public user, role_id == 1
        if (sessionManager.fetchUserRoleId() == "1") {
            navView.menu.getItem(0).isVisible = false
            navView.menu.getItem(3).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        //menu for Administrator, role_id == 3
        if (sessionManager.fetchUserRoleId() == "3") {
            navView.menu.getItem(0).isVisible = false
            navView.menu.getItem(1).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        //menu for dispatcher, role_id == 4 or manager, role_id == 5
        if (sessionManager.fetchUserRoleId() == "4" || sessionManager.fetchUserRoleId() == "5") {
            navView.menu.getItem(0).isVisible = false
            navView.menu.getItem(1).isVisible = false
            navView.menu.getItem(3).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        ///////////////END OF MENU STUFF////////////////////////////////////////////

        if (sessionManager.fetchUserRoleId()?.toInt()!! == 5)
            findViewById<Button>(R.id.deleteButton).visibility = View.GONE

        if (sessionManager.fetchUserRoleId()?.toInt()!! < 3)
        {
            findViewById<TextView>(R.id.zamestnanecLabel).visibility = View.GONE
            findViewById<Spinner>(R.id.zamestnanecSpinner).visibility = View.GONE

            findViewById<TextView>(R.id.prioritaLabel).visibility = View.GONE
            findViewById<Spinner>(R.id.prioritaSpinner).visibility = View.GONE

            findViewById<TextView>(R.id.priradeneVozidloLabel).visibility = View.GONE
            findViewById<Spinner>(R.id.priradeneVozidloSpinner).visibility = View.GONE
        }




        verejne = findViewById(R.id.checkBox1)

        if (sessionManager.fetchUserRoleId()?.toInt()!! < 3)
            verejne.visibility = View.GONE

        extras = intent.extras
        if (null != extras) {
            idProblem = extras!!.getInt("problemID")
            from = extras!!.getInt("from")
        }
        getAll()


        if (from == 1)
            findViewById<Button>(R.id.zrusitButton).setOnClickListener {
                Intent(this, ProblemListActivity::class.java).apply {
                    putExtra("allProblems", 1)
                    startActivity(this)
                }
            }
        if (from == 0)
            findViewById<Button>(R.id.zrusitButton).setOnClickListener {
                Intent(this, ProblemListActivity::class.java).apply {
                    putExtra("allProblems", 0)
                    startActivity(this)
                }
            }
        if (from == 3)
            findViewById<Button>(R.id.zrusitButton).setOnClickListener {
                Intent(this, MapsActivity::class.java).apply {
                    startActivity(this)
                }
            }

        imageView = findViewById(R.id.fotoRiesenia)
        findViewById<Button>(R.id.fotoButton).setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        //delete problem and all asociations //idProblem
        findViewById<Button>(R.id.deleteButton).setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            //set title for alert dialog
            builder.setTitle("Potvrďte vymazanie")
            //set message for alert dialog
            builder.setMessage("Naozaj chcete natrvalo odstrániť problém?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton(R.string.Yes){dialogInterface, which ->
                // Delete selected note from database

                val deleteRequest = DeleteRequest(sessionManager.fetchAuthToken().toString(), idProblem)
                val request = ServiceBuilder.buildService(CallsAPI::class.java)
                val call =
                    request.delete(deleteRequest)

                call.enqueue(object : Callback<Int> {
                    override fun onResponse(
                        call: Call<Int>,
                        response: Response<Int>
                    ) {
                        val suc = response.body()
                        if (suc == 1)
                            Toast.makeText(
                                this@DetailActivity,
                                "Uspesne vymazane",
                                Toast.LENGTH_LONG
                            ).show()
                        else
                            Toast.makeText(
                                this@DetailActivity,
                                "Ups, something went wrong!",
                                Toast.LENGTH_LONG
                            ).show()
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                dialogInterface.dismiss()
            }
            //performing negative action
            val x = builder.setNegativeButton("Nie"){ dialogInterface, which ->
                dialogInterface.dismiss()
            }
            x.show()
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()


        }

        //aktualizacia problemu
        findViewById<Button>(R.id.potvrditButton).setOnClickListener {
            //kontrola zmenenych udajov zamestnanc
            if (zamestnanec != spinnerZamestnanec.selectedItem.toString())
                zamestnanecChange = spinnerZamestnanec.selectedItem.toString()
            else
                zamestnanecChange = "n"

            //kontrola zmenenych udajov priorita
            if (priorita != spinnerPriorita.selectedItem.toString())
                prioritaChange = spinnerPriorita.selectedItem.toString()
            else
                prioritaChange = "n"

            //kontrola zmenenych udajov kategoria
            if (kategoria != spinnerKategoria.selectedItem.toString())
                kategoriaChange = spinnerKategoria.selectedItem.toString()
            else
                kategoriaChange = "n"

            //kontrola zmenenych udajov stav
            if (stavProblemu != spinnerStavProblemu.selectedItem.toString())
                stavChange = spinnerStavProblemu.selectedItem.toString()
            else
                stavChange = "n"

            //kontrola zmenenych udajov stavRiesenia
            if (stavRiesenia != spinnerStavRieseniaProblemu.selectedItem.toString())
                stavRieseniaChange = spinnerStavRieseniaProblemu.selectedItem.toString()
            else
                stavRieseniaChange = "n"

            //kontrola zmenenych udajov priradene vozidlo
            if (vozidlo != spinnerVozidlo.selectedItem.toString())
                priradeneVozidloChange = spinnerVozidlo.selectedItem.toString()
            else
                priradeneVozidloChange = "n"

            //kontrola zmenenych udajov opis Riesenia
            if (popisRiesenia != findViewById<TextInputEditText>(R.id.poznmkaField).text.toString() &&
                findViewById<TextInputEditText>(R.id.poznmkaField).text.toString() != "")
                opisRieseniaChange = findViewById<TextInputEditText>(R.id.poznmkaField).text.toString()
            else
                opisRieseniaChange = "n"


            Toast.makeText(
                this@DetailActivity, "Loading...",
                Toast.LENGTH_LONG
            ).show()

            send()

        }
        registerForContextMenu(findViewById<Button>(R.id.historiaButton))
        findViewById<Button>(R.id.historiaButton).setOnClickListener {
            openContextMenu(findViewById<Button>(R.id.historiaButton))
            true
        }

        findViewById<Button>(R.id.komentButton).setOnClickListener {
            val intent2 = Intent(this, CommentActivity::class.java)
            intent2.putExtra("from", from)
            intent2.putExtra("problemID", idProblem)
            startActivity(intent2)
        }

    }
    // riesenieImg = 0, komentar = 1, popisStavuRieseniaProblemu = 2,
    //priradeneVozidlo = 3, priradenyZamestnanec = 4, stavRieseniaProblemu = 5,
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Vyberte zobrazenie historie")
        menu?.add(0, v?.id!!, 0, "Fotky riesenia problemu")
        menu?.add(0, v?.id!!, 1, "Komentare")
        menu?.add(1, v?.id!!, 0, "popisy stavu riesenia")
        menu?.add(1, v?.id!!, 0, "priradene vozidla")
        menu?.add(1, v?.id!!, 0, "priradeni zamestnanci")
        menu?.add(1, v?.id!!, 0, "stavy riesenia problemu")

    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.layout.popup_menu, menu)
        return true
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {

        when {
            item.title == "Fotky riesenia problemu" -> {
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 0)
                startActivity(intent2)
                return true
            }
            item.title == "Komentare" -> {
                finish()
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 1)
                startActivity(intent2)
                return true
            }
            item.title == "popisy stavu riesenia" -> {
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 2)
                startActivity(intent2)
                return true
            }
            item.title == "priradene vozidla" -> {
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 3)
                startActivity(intent2)
                return true
            }
            item.title == "priradeni zamestnanci" -> {
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 4)
                startActivity(intent2)
                return true
            }
            item.title == "stavy riesenia problemu" -> {
                val intent2 = Intent(this, HistoryActivity::class.java)
                intent2.putExtra("from", from)
                intent2.putExtra("problemID", idProblem)
                intent2.putExtra("attribute", 5)
                startActivity(intent2)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    fun refresh()
    {
        Intent(this, DetailActivity::class.java).apply {
            putExtra("problemID", idProblem)
            putExtra("from", from)
            startActivity(this)
        }
    }

    fun send()
    {
        var verejneValue = 0
        if (verejne.isChecked)
            verejneValue = 1

        val request = ServiceBuilder.buildService(CallsAPI::class.java)
        val call = request.editProblem(EditProblem(zamestnanecChange, prioritaChange,
            kategoriaChange, stavChange, stavRieseniaChange, priradeneVozidloChange, opisRieseniaChange, sessionManager.fetchAuthToken().toString(), problemID = idProblem, verejneValue))
        call.enqueue(object : Callback<Int> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if (response.body() != -1) {
                    if (imageUri != null && opisRieseniaChange != "n")
                        uploadRiesenieImg(response.body())
                    if (imageUri != null && opisRieseniaChange == "n")
                    {
                        val builder = AlertDialog.Builder(this@DetailActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Takto nie...")
                        //set message for alert dialog
                        builder.setMessage("Pre pridanie fotky riešenia, prosím pridajte poznamku k riešeniu")
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setNeutralButton("Ok"){dialogInterface , which ->
                            dialogInterface.dismiss()
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }
                    if (imageUri == null)
                    {
                        refresh()
                    }
                }
                else{
                    val builder = AlertDialog.Builder(this@DetailActivity, R.style.AlertDialogTheme)
                    //set title for alert dialog
                    builder.setTitle("HUUPS...")
                    //set message for alert dialog
                    builder.setMessage("Aktualizácia problému zlyhala... skúste prosim znovu")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing cancel action
                    builder.setNeutralButton("Ok"){dialogInterface , which ->
                        dialogInterface.dismiss()
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadRiesenieImg(popisID : Int?)
    {
        fun getRandomString(length: Int = 10) : String {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { charset.random() }
                .joinToString("")
        }
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter).toString()
        val randomString = getRandomString() + formatted

        //upload img
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val name = randomString.toRequestBody("text/plain".toMediaTypeOrNull())

        val request = ServiceBuilder.buildService(CallsAPI::class.java)
        val req = request.postRiesenieImg(body, name, sessionManager.fetchAuthToken().toString().toRequestBody("text/plain".toMediaTypeOrNull()), popisID.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        /////////////////////////////////////
        req.enqueue(object : Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            ) {
                if (response.body() == 1)
                    refresh()
                if (response.body() == 0) {
                    val builder = AlertDialog.Builder(this@DetailActivity, R.style.AlertDialogTheme)
                    //set title for alert dialog
                    builder.setTitle("HUUPS...")
                    //set message for alert dialog
                    builder.setMessage("Pridanie fotky neprebehlo v poriadku")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing cancel action
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                        dialogInterface.dismiss()
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                val builder = AlertDialog.Builder(this@DetailActivity, R.style.AlertDialogTheme)
                //set title for alert dialog
                builder.setTitle("HUUPS...Príliž veĺké!")
                //set message for alert dialog
                builder.setMessage("Fotka nesmie mať viac ako 2MB")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing cancel action
                builder.setNeutralButton("Ok"){dialogInterface , which ->
                    dialogInterface.dismiss()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        })
    }

    fun getAll(){
        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems(idProblem, "-", "-", "-", "0000-00-00", "0000-00-00",
            "-","-", "-", sessionManager.fetchUserRoleId()?.toInt())
        call!!.enqueue(object : Callback<List<ShowAllProblemsData?>?> {
            override fun onResponse(
                call: Call<List<ShowAllProblemsData?>?>,
                response: Response<List<ShowAllProblemsData?>?>
            ) {

                val problemList = response.body()
                for (item in problemList!!) {

                    datum = item!!.created_at
                    vytvoril = item.pouzivatel_meno
                    popis = item.popis
                    zamestnanec = item.zamestnanec
                    priorita = item.priorita
                    poloha = item.position
                    kategoria = item.kategoria
                    stavProblemu = item.stav_problemu
                    stavRiesenia = item.stav_riesenia_problemu
                    vozidlo = item.vozidlo
                    popisRiesenia = item.popis_riesenia_problemu

                }


                if (popisRiesenia == "neuvedene") {
                    findViewById<TextInputEditText>(R.id.poznmkaField).setText(" ")
                }



                getSpinners()

            }
            override fun onFailure(call: Call<List<ShowAllProblemsData?>?>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }

    fun getSpinners(){

        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getSpinners()
        call!!.enqueue(object : Callback<List<Spinners>> {
            override fun onResponse(
                call: Call<List<Spinners>>,
                response: Response<List<Spinners>>
            ) {

                val spinnersList = response.body()
                for (item in spinnersList!!)
                {
                    zamestnanci = item.zamestnanci
                    priority = item.priority
                    kategorie = item.kategorie
                    stavyProblemu = item.stavy_problemu
                    stavyRiesenia = item.stavy_riesenia
                    vozidla = item.vozidla
                }



                //insertValues()
                getImgs()

            }

            override fun onFailure(call: Call<List<Spinners>>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun getImgs(){

        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getImg(idProblem)
        call.enqueue(object : Callback<List<Imgs>> {
            override fun onResponse(
                call: Call<List<Imgs>>,
                response: Response<List<Imgs>>
            ) {
                val urls = response.body()
                var i = 0
                if (urls != null)
                    for (item in urls) {
                        imgProblemUrl = item.urlProblem
                        imgRiesenieUrl = item.urlRiesenie

                    }
                insertValues()
            }

            override fun onFailure(call: Call<List<Imgs>>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    @SuppressLint("CutPasteId")
    fun insertValues(){

        if (imgProblemUrl != "n" && imgProblemUrl != "")
            Picasso.get().load("http://147.175.204.24/$imgProblemUrl").into(findViewById<ImageView>(R.id.imageView))
        if (imgRiesenieUrl != "n" && imgRiesenieUrl != "")
            Picasso.get().load("http://147.175.204.24/$imgRiesenieUrl").into(findViewById<ImageView>(R.id.fotoRiesenia))

        spinnerZamestnanec = findViewById(R.id.zamestnanecSpinner)
        spinnerPriorita = findViewById(R.id.prioritaSpinner)
        spinnerKategoria = findViewById(R.id.kategoriaSpinner)
        spinnerStavProblemu = findViewById(R.id.stavProblemuSpinner)
        spinnerStavRieseniaProblemu = findViewById(R.id.stavRieseniaProblemuSpinner)
        spinnerVozidlo = findViewById(R.id.priradeneVozidloSpinner)

        //access for roles
        if (sessionManager.fetchUserRoleId() == "2" || sessionManager.fetchUserRoleId() == "1")
        {
            findViewById<Button>(R.id.fotoButton).isVisible = false
            findViewById<Button>(R.id.deleteButton).isVisible = false
            findViewById<Button>(R.id.potvrditButton).isVisible = false

            spinnerZamestnanec.isClickable = false
            spinnerZamestnanec.isEnabled = false
            spinnerPriorita.isClickable = false
            spinnerPriorita.isEnabled = false
            spinnerKategoria.isClickable = false
            spinnerKategoria.isEnabled = false
            spinnerStavProblemu.isClickable = false
            spinnerStavProblemu.isEnabled = false
            spinnerStavRieseniaProblemu.isClickable = false
            spinnerStavRieseniaProblemu.isEnabled = false
            spinnerVozidlo.isClickable = false
            spinnerVozidlo.isEnabled = false
            findViewById<TextInputEditText>(R.id.poznmkaField).isClickable = false
            findViewById<TextInputEditText>(R.id.poznmkaField).isEnabled = false
        }

        if (sessionManager.fetchUserRoleId() == "3" || sessionManager.fetchUserRoleId() == "4" ||
                sessionManager.fetchUserRoleId() == "5")
        {
            findViewById<Button>(R.id.komentButton).isVisible = false

            if (sessionManager.fetchUserRoleId() == "4")
            {
                spinnerZamestnanec.isClickable = false
                spinnerZamestnanec.isEnabled = false
            }
        }



        val adapter = ArrayAdapter(
            this,
            simple_spinner_item, zamestnanci
        )
        spinnerZamestnanec.adapter = adapter

        var i = -1
        for (item in zamestnanci)
        {
            i++
            if (item == zamestnanec)
                spinnerZamestnanec.setSelection(i)
        }
        spinnerZamestnanec.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val adapter2 = ArrayAdapter(
            this,
            simple_spinner_item, priority
        )
        spinnerPriorita.adapter = adapter2

        i = -1
        for (item in priority)
        {
            i++
            if (item == priorita)
                spinnerPriorita.setSelection(i)
        }
        spinnerPriorita.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val adapter3 = ArrayAdapter(
            this,
            simple_spinner_item, kategorie
        )
        spinnerKategoria.adapter = adapter3

        i = -1
        for (item in kategorie)
        {
            i++
            if (item == kategoria)
                spinnerKategoria.setSelection(i)
        }
        spinnerKategoria.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val adapter4 = ArrayAdapter(
            this,
            simple_spinner_item, stavyProblemu
        )
        spinnerStavProblemu.adapter = adapter4

        i = -1
        for (item in stavyProblemu)
        {
            i++
            if (item == stavProblemu)
                spinnerStavProblemu.setSelection(i)
        }
        spinnerStavProblemu.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val adapter5 = ArrayAdapter(
            this,
            simple_spinner_item, stavyRiesenia
        )
        spinnerStavRieseniaProblemu.adapter = adapter5

        i = -1
        for (item in stavyRiesenia)
        {
            i++
            if (item == stavRiesenia)
                spinnerStavRieseniaProblemu.setSelection(i)
        }
        spinnerStavRieseniaProblemu.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val adapter6 = ArrayAdapter(
            this,
            simple_spinner_item, vozidla
        )
        spinnerVozidlo.adapter = adapter6

        i = -1
        for (item in vozidla)
        {
            i++
            if (item == vozidlo)
                spinnerVozidlo.setSelection(i)
        }
        spinnerVozidlo.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val datumText: TextView = findViewById(R.id.datumValue)
        datumText.text = datum

        val vytvorilText: TextView = findViewById(R.id.menoValue)
        vytvorilText.text = vytvoril

        val popistext: TextView = findViewById(R.id.popisValue)
        popistext.text = popis

        val polohaText: TextView = findViewById(R.id.polohaValue)
        polohaText.text = poloha

        val popisRieseniaText: TextInputEditText = findViewById(R.id.poznmkaField)
        popisRieseniaText.setText(popisRiesenia)

        if (popisRiesenia == "neuvedene") {
            findViewById<TextInputEditText>(R.id.poznmkaField).setText(" ")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            val selectedImage: Uri? = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null) ?: return
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            file = File(filePath)
        }
    }

}

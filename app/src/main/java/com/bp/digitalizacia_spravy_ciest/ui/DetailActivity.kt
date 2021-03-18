package com.bp.digitalizacia_spravy_ciest.ui

import android.R.layout.simple_spinner_item
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.ShowAllProblemsData
import com.bp.digitalizacia_spravy_ciest.models.Spinners
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    var extras : Bundle? = null
    private var idProblem : Int = 0
    var loged = 0

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
                    Toast.makeText(this, "zoznam pouzivatelov", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "registracia", Toast.LENGTH_SHORT).show()
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

        extras = intent.extras
        if (null != extras) {
            idProblem = extras!!.getInt("problemID")
        }
        getAll()
    }

    fun getAll(){
        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems(idProblem)
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
                Toast.makeText(
                    this@DetailActivity,
                    zamestnanec,
                    Toast.LENGTH_LONG
                ).show()
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

                Toast.makeText(
                    this@DetailActivity,
                    "test2",
                    Toast.LENGTH_LONG
                ).show()

                insertValues()

            }

            override fun onFailure(call: Call<List<Spinners>>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    fun insertValues(){

        Toast.makeText(
            this@DetailActivity,
            "test3",
            Toast.LENGTH_LONG
        ).show()

        spinnerZamestnanec = findViewById(R.id.zamestnanecSpinner)
        spinnerPriorita = findViewById(R.id.prioritaSpinner)
        spinnerKategoria = findViewById(R.id.kategoriaSpinner)
        spinnerStavProblemu = findViewById(R.id.stavProblemuSpinner)
        spinnerStavRieseniaProblemu = findViewById(R.id.stavRieseniaProblemuSpinner)
        spinnerVozidlo = findViewById(R.id.priradeneVozidloSpinner)

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

    }
}

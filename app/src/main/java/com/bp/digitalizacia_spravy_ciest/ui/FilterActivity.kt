package com.bp.digitalizacia_spravy_ciest.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.Spinners
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.filter_layout.*
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FilterActivity : AppCompatActivity(){

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    private lateinit var zamestnanci : List<String>
    private lateinit var priority : List<String>
    private lateinit var kategorie : List<String>
    private lateinit var stavyProblemu : List<String>
    private lateinit var stavyRiesenia : List<String>
    private lateinit var vozidla : List<String>

    private lateinit var zamestnanciMutable : MutableList<String>
    private lateinit var priorityMutable : MutableList<String>
    private lateinit var kategorieMutable : MutableList<String>
    private lateinit var stavyProblemuMutable : MutableList<String>
    private lateinit var stavyRieseniaMutable : MutableList<String>
    private lateinit var vozidlaMutable : MutableList<String>

    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    var button_date2: Button? = null
    var textview_date2: TextView? = null
    var cal2 = Calendar.getInstance()

    var extras : Bundle? = null

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

    lateinit var spinnerZamestnanec: Spinner
    lateinit var spinnerPriorita: Spinner
    lateinit var spinnerKategoria: Spinner
    lateinit var spinnerStavProblemu: Spinner
    lateinit var spinnerVozidlo: Spinner
    lateinit var spinnerStavRieseniaProblemu: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.filter_layout)

        //allProblems 1 or myProblems 0
        extras = intent.extras
        val allProblems = extras!!.getInt("allProblems")


        /////////////////////////////MENU STUFF/////////////////////////////////
        //hamburger for side menu drawer
        val hamburger = this.findViewById<ImageView>(R.id.imageViewLP)
        hamburger?.setOnClickListener()
        {
            this.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Call findViewById on the DrawerLayout
        this.drawerLayout = this.findViewById(R.id.drawerLayoutLP)

        // Pass the ActionBarToggle action into the drawerListener
        this.actionBarToggle = ActionBarDrawerToggle(this, this.drawerLayout, 0, 0)
        this.drawerLayout.addDrawerListener(this.actionBarToggle)

        // Display the hamburger icon to launch the drawer
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        this.actionBarToggle.syncState()

        // Call findViewById on the NavigationView
        this.navView = this.findViewById(R.id.navViewLP)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        this.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuNastavenia -> {
                    Toast.makeText(this, "nastavenia", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menuMojeProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    intent2.putExtra("allProblems", 0)
                    this.startActivity(intent2)
                    true
                }
                R.id.menuVsetkyProblemy -> {
                    val intent2 = Intent(this, ProblemListActivity::class.java)
                    intent2.putExtra("allProblems", 1)
                    this.startActivity(intent2)
                    true
                }
                R.id.menuZoznamPouzivatelov -> {
                    val intent2 = Intent(this, UsersListActivity::class.java)
                    this.startActivity(intent2)
                    true
                }
                R.id.mapFragment4 -> {
                    Intent(this, MapsActivity::class.java).apply {
                        this@FilterActivity.startActivity(this)
                    }
                    true
                }
                R.id.menuPrihlasenie -> {
                    Intent(this, LoginActivity::class.java).apply {
                        this@FilterActivity.startActivity(this)
                    }
                    true
                }
                R.id.menuRegistracia -> {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://147.175.204.24/register"))
                    startActivity(i)
                    true
                }
                R.id.menuOdhlasenie -> {
                    this.sessionManager.clear()
                    Intent(this, MapsActivity::class.java).apply {
                        this@FilterActivity.startActivity(this)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        this.sessionManager = SessionManager(this)
        //Actual user info
        val header = this.navView.getHeaderView(0)
        header.userName.text = this.sessionManager.fetchUserName().toString()

        //menu for unregistered user, role_id == 2
        if (this.sessionManager.fetchUserId() == null) {
            header.prihlaseny.visibility = View.GONE
            this.navView.menu.getItem(0).isVisible = false
            this.navView.menu.getItem(1).isVisible = false
            this.navView.menu.getItem(3).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(1).isVisible = false
        }

        //menu for registred public user, role_id == 1
        if (this.sessionManager.fetchUserRoleId() == "1") {
            this.navView.menu.getItem(0).isVisible = false
            this.navView.menu.getItem(3).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        //menu for Administrator, role_id == 3
        if (this.sessionManager.fetchUserRoleId() == "3") {
            this.navView.menu.getItem(0).isVisible = false
            this.navView.menu.getItem(1).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        //menu for dispatcher, role_id == 4 or manager, role_id == 5
        if (this.sessionManager.fetchUserRoleId() == "4" || this.sessionManager.fetchUserRoleId() == "5") {
            this.navView.menu.getItem(0).isVisible = false
            this.navView.menu.getItem(1).isVisible = false
            this.navView.menu.getItem(3).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(0).isVisible = false
            this.navView.menu.getItem(5).subMenu.getItem(2).isVisible = false
        }

        ///////////////END OF MENU STUFF////////////////////////////////////////////

        // get the references from layout file
        textview_date = this.datumOd
        button_date = this.datumOdButton

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        }



        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@FilterActivity,
                    R.style.DatePickerTheme_Dark,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()


            }

        })

        // get the references from layout file
        textview_date2 = this.DatumDo
        button_date2 = this.DatumDoButton

        // create an OnDateSetListener
        val dateSetListener2 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal2.set(Calendar.YEAR, year)
                cal2.set(Calendar.MONTH, monthOfYear)
                cal2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView2()
            }

        }



        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date2!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@FilterActivity,
                    R.style.DatePickerTheme_Dark,
                    dateSetListener2,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal2.get(Calendar.YEAR),
                    cal2.get(Calendar.MONTH),
                    cal2.get(Calendar.DAY_OF_MONTH)
                ).show()


            }

        })

        getSpinners()

        findViewById<Button>(R.id.pouzitFilter).setOnClickListener {
            val intent2 = Intent(this, ProblemListActivity::class.java)
            intent2.putExtra("allProblems", allProblems)
            intent2.putExtra("zamestnanecFilter", spinnerZamestnanec.selectedItem.toString())
            intent2.putExtra("stavProblemuFilter", spinnerStavProblemu.selectedItem.toString())
            intent2.putExtra("kategoriaFilter", spinnerKategoria.selectedItem.toString())
            intent2.putExtra("DatumOdFilter", textview_date!!.text.toString())
            intent2.putExtra("DatumDoFilter", textview_date2!!.text.toString())
            intent2.putExtra("vozidloFilter", spinnerVozidlo.selectedItem.toString())
            intent2.putExtra("prioritaFilter", spinnerPriorita.selectedItem.toString())
            intent2.putExtra("stavRieseniaFilter", spinnerStavRieseniaProblemu.selectedItem.toString())
            startActivity(intent2)
        }

        findViewById<Button>(R.id.filterSpat).setOnClickListener {
            val intent2 = Intent(this, ProblemListActivity::class.java)
            intent2.putExtra("allProblems", allProblems)
            startActivity(intent2)
        }

    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    private fun updateDateInView2() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date2!!.text = sdf.format(cal2.getTime())
    }

    fun getSpinners(){

        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getSpinners()
        call.enqueue(object : Callback<List<Spinners>> {
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
                zamestnanciMutable = zamestnanci.toMutableList()
                zamestnanciMutable.add(0, "-")

                priorityMutable = priority.toMutableList()
                priorityMutable.add(0, "-")

                kategorieMutable = kategorie.toMutableList()
                kategorieMutable.add(0,"-")

                stavyProblemuMutable = stavyProblemu.toMutableList()
                stavyProblemuMutable.add(0, "-")

                stavyRieseniaMutable = stavyRiesenia.toMutableList()
                stavyRieseniaMutable.add(0,"-")

                vozidlaMutable = vozidla.toMutableList()
                vozidlaMutable.add(0, "-")

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

    @SuppressLint("CutPasteId")
    fun insertValues(){

        spinnerZamestnanec = findViewById(R.id.zamestnanecSpinnerFilter)
        spinnerPriorita = findViewById(R.id.prioritaSpinnerFilter)
        spinnerKategoria = findViewById(R.id.kategoriaSpinnerFilter)
        spinnerStavProblemu = findViewById(R.id.stavProblemuSpinnerFilter)
        spinnerStavRieseniaProblemu = findViewById(R.id.stavRieseniaProblemuSpinnerFilter)
        spinnerVozidlo = findViewById(R.id.priradeneVozidloSpinnerFilter)

        //access for roles
        if (sessionManager.fetchUserRoleId() == "2" || sessionManager.fetchUserRoleId() == "1")
        {
            spinnerZamestnanec.isVisible = false
            findViewById<TextView>(R.id.zamestnanecLabel).isVisible = false
            spinnerPriorita.isVisible = false
            findViewById<TextView>(R.id.prioritaLabel).isVisible = false
            spinnerVozidlo.isVisible = false
            findViewById<TextView>(R.id.priradeneVozidloLabel).isVisible = false
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, zamestnanciMutable
        )
        spinnerZamestnanec.adapter = adapter

        var i = -1
        for (item in zamestnanciMutable)
        {
            i++
            if (item == "-")
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
            android.R.layout.simple_spinner_item, priorityMutable
        )
        spinnerPriorita.adapter = adapter2

        i = -1
        for (item in priorityMutable)
        {
            i++
            if (item == "-")
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
            android.R.layout.simple_spinner_item, kategorieMutable
        )
        spinnerKategoria.adapter = adapter3

        i = -1
        for (item in kategorieMutable)
        {
            i++
            if (item == "-")
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
            android.R.layout.simple_spinner_item, stavyProblemuMutable
        )
        spinnerStavProblemu.adapter = adapter4

        i = -1
        for (item in stavyProblemuMutable)
        {
            i++
            if (item == "-")
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
            android.R.layout.simple_spinner_item, stavyRieseniaMutable
        )
        spinnerStavRieseniaProblemu.adapter = adapter5

        i = -1
        for (item in stavyRieseniaMutable)
        {
            i++
            if (item == "-")
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
            android.R.layout.simple_spinner_item, vozidlaMutable
        )
        spinnerVozidlo.adapter = adapter6

        i = -1
        for (item in vozidlaMutable)
        {
            i++
            if (item == "-")
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

    }
}
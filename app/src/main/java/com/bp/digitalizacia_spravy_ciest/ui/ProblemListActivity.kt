package com.bp.digitalizacia_spravy_ciest.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.adapters.MyListAdapter
import com.bp.digitalizacia_spravy_ciest.models.ShowAllProblemsData
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import kotlinx.android.synthetic.main.problems_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ProblemListActivity : AppCompatActivity() {

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    var extras : Bundle? = null

    private var zamestnanecFilter: String = "-"
    private var stavProblemuFilter: String = "-"
    private var kategoriaFilter: String = "-"
    private var DatumOdFilter: String = "0000-00-00"
    private var DatumDoFilter: String = "0000-00-00"
    private var vozidloFilter: String = "-"
    private var prioritaFilter: String = "-"
    private var stavRieseniaFilter: String = "-"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problems_list)

        var pocet: Int

        //allProblems 1 or myProblems 0
        extras = intent.extras
        val allProblems = extras!!.getInt("allProblems")

        if (extras!!.getString("zamestnanecFilter") != null)
        {
            zamestnanecFilter = extras!!.getString("zamestnanecFilter").toString()
            stavProblemuFilter = extras!!.getString("stavProblemuFilter").toString()
            kategoriaFilter = extras!!.getString("kategoriaFilter").toString()
            DatumOdFilter = extras!!.getString("DatumOdFilter").toString()
            DatumDoFilter = extras!!.getString("DatumDoFilter").toString()
            vozidloFilter = extras!!.getString("vozidloFilter").toString()
            prioritaFilter = extras!!.getString("prioritaFilter").toString()
            stavRieseniaFilter = extras!!.getString("stavRieseniaFilter").toString()

        }

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

        findViewById<Button>(R.id.zrusitFilter).setOnClickListener {
            val intent2 = Intent(this, ProblemListActivity::class.java)
            intent2.putExtra("allProblems", allProblems)
            startActivity(intent2)
        }


        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems(0, zamestnanecFilter,stavProblemuFilter, kategoriaFilter,
            DatumOdFilter, DatumDoFilter, vozidloFilter, prioritaFilter, stavRieseniaFilter,
            sessionManager.fetchUserRoleId()?.toInt())
        call!!.enqueue(object : Callback<List<ShowAllProblemsData?>?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<ShowAllProblemsData?>?>,
                response: Response<List<ShowAllProblemsData?>?>
            ) {
                if (response.body() != null) {
                    Toast.makeText(
                        this@ProblemListActivity,
                        "Zaznamy uspesne zobrazene",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        this@ProblemListActivity,
                        "jojojj",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val problemList = response.body()
                pocet = 0

                if (allProblems == 1 && problemList != null)
                    pocet = problemList.size

                if (allProblems == 0)
                    for (item in problemList!!)
                    {
                        if (item!!.pouzivatel == sessionManager.fetchUserId()!!.toBigInteger())
                            pocet += 1

                    }

                var categories = arrayOfNulls<String>(pocet)
                var dates = arrayOfNulls<LocalDate>(pocet)
                var IDs = arrayOfNulls<BigInteger>(pocet)

                if (problemList != null) {
                    var i = 0
                    if (allProblems == 0) {
                        for (item in problemList) {
                            if (item!!.pouzivatel == sessionManager.fetchUserId()!!
                                    .toBigInteger() && allProblems == 0
                            ) {
                                IDs[pocet - i - 1] = item!!.id
                                categories[pocet - i - 1] = item.kategoria
                                dates[pocet - i - 1] = LocalDate.parse(
                                    item.created_at,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                )
                                i += 1
                            }
                        }
                    }
                    if (allProblems == 1) {
                        for (item in problemList) {

                            IDs[pocet - i - 1] = item!!.id
                            categories[pocet - i - 1] = item.kategoria
                            dates[pocet - i - 1] = LocalDate.parse(item.created_at,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            i += 1
                        }
                    }

                }
                val myListAdapter = MyListAdapter(this@ProblemListActivity, IDs, categories, dates, pocet)
                problemlist.adapter = myListAdapter
            }

            override fun onFailure(call: Call<List<ShowAllProblemsData?>?>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        problemlist.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            val problemID = view.findViewById<TextView>(R.id.problemID).text
            Intent(this, DetailActivity::class.java).apply {
                putExtra("problemID", problemID)
                putExtra("from", allProblems)
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.vybratFilter).setOnClickListener()
        {
            Intent(this, FilterActivity::class.java).apply {
                putExtra("allProblems", allProblems)
                startActivity(this)
            }

        }

    }


}
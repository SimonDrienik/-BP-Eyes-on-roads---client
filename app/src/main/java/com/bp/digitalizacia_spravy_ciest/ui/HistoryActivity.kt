package com.bp.digitalizacia_spravy_ciest.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.adapters.HistoryAdapter
import com.bp.digitalizacia_spravy_ciest.adapters.HistoryAdapterImg
import com.bp.digitalizacia_spravy_ciest.models.ShowHistory
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.history_activity.*
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import kotlinx.android.synthetic.main.problems_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryActivity : AppCompatActivity() {

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    private var extras: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)

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
        val from = extras!!.getInt("from")
        val attribute = extras!!.getInt("attribute")
        val problemID = extras!!.getInt("problemID")

        findViewById<Button>(R.id.zrusit).setOnClickListener {
            val intent2 = Intent(this, DetailActivity::class.java)
            intent2.putExtra("from", from)
            intent2.putExtra("problemID", problemID)
            startActivity(intent2)
        }

        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        // riesenieImg = 0, komentar = 1, popisStavuRieseniaProblemu = 2,
        //priradeneVozidlo = 3, priradenyZamestnanec = 4, stavRieseniaProblemu = 5,
        val call = request.getHistory(attribute, problemID)
        call.enqueue(object : Callback<List<ShowHistory>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<ShowHistory>>,
                response: Response<List<ShowHistory>>
            ) {
                if (response.body() != null) {
                    Toast.makeText(
                        this@HistoryActivity,
                        "Zaznamy uspesne zobrazene",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val problemList = response.body()
                val pocet : Int
                if (problemList!!.size < 1) {
                    pocet = 1
                }
                else {
                    pocet = problemList.size
                }

                val names = arrayOfNulls<String>(pocet-1)
                val dates = arrayOfNulls<LocalDate>(pocet-1)
                val users = arrayOfNulls<String>(pocet-1)

                if (problemList.size > 1) {
                    var i = 0
                    for (item in problemList) {
                        if (i < pocet - 1) {
                            names[pocet - i - 2] = item.name
                            dates[pocet - i - 2] = LocalDate.parse(
                                item.created_at,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                            users[pocet - i - 2] = item.user
                        }
                        i += 1
                    }
                }
                if (attribute != 0) {
                    val historyAdapter = HistoryAdapter(this@HistoryActivity, names, dates, pocet-1)
                    historylist.adapter = historyAdapter
                }
                else {
                    val historyAdapter = HistoryAdapterImg(this@HistoryActivity, names, dates, pocet-1, users)
                    historylist.adapter = historyAdapter
                }

            }

            override fun onFailure(call: Call<List<ShowHistory>>, t: Throwable) {
                Toast.makeText(
                    applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }
}
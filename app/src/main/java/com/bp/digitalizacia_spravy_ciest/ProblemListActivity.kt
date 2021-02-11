package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
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

    var extras : Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problems_list)

        extras = intent.extras
        val pocet= extras!!.getInt("pocet")

        //arrays of problems in list
        var categories = arrayOfNulls<String>(pocet)
        var dates = arrayOfNulls<LocalDate>(pocet)
        var IDs = arrayOfNulls<BigInteger>(pocet)
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
                    Intent(this, ProblemListActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }
                R.id.menuVsetkyProblemy -> {
                    Intent(this, ProblemListActivity::class.java).apply {
                        startActivity(this)
                    }
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
                else -> {
                    false
                }
            }
        }
        ///////////////END OF MENU STUFF////////////////////////////////////////////


        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems()
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
                }
                val problemList = response.body()
                if (problemList != null) {
                    var i = 0
                    for (item in problemList) {
                        IDs[pocet - i - 1] = item!!.id
                        categories[pocet - i - 1] = item.kategoria
                        dates[pocet - i - 1] = LocalDate.parse(item.created_at, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        i += 1
                    }

                    Toast.makeText(
                        this@ProblemListActivity,
                        "tu to funguje",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val myListAdapter = MyListAdapter(this@ProblemListActivity, IDs, categories, dates, pocet)
                problemlist.adapter = myListAdapter
                Toast.makeText(
                    this@ProblemListActivity,
                    "aj totok",
                    Toast.LENGTH_SHORT
                ).show()
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
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
        }

    }


}
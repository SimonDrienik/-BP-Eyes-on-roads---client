package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProblemListActivity : AppCompatActivity() {

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problems_list)

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



        getall()
        // use arrayadapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "item 1", "item 2", "item 3",
            "item 4", "item 5"
        )

        // access the listView from xml file
        val mListView = findViewById<ListView>(R.id.problemlist)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, users)
        mListView.adapter = arrayAdapter
    }

    fun getall(){
        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getProblems()
        call!!.enqueue(object : Callback<List<ShowAllProblemsData?>?> {
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
                        var id_problemu = item?.id
                        var kategoria = item?.kategoria
                        var created_at = item?.created_at
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
}
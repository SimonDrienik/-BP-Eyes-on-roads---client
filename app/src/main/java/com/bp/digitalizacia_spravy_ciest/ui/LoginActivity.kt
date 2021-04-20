package com.bp.digitalizacia_spravy_ciest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.LoginRequest
import com.bp.digitalizacia_spravy_ciest.models.LoginResponse
import com.bp.digitalizacia_spravy_ciest.models.User
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ServiceBuilder
    private var email: String = ""
    private var pswd: String = ""

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

     var loged = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)


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

        var et_user_name = findViewById(R.id.et_user_name) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_reset = findViewById(R.id.btn_reset) as Button
        var btn_submit = findViewById(R.id.btn_submit) as Button

        btn_reset.setOnClickListener {
            et_user_name.setText("")
            et_password.setText("")
        }

        btn_submit.setOnClickListener {

            apiClient = ServiceBuilder
            sessionManager = SessionManager(this)

            val request = ServiceBuilder.buildService(CallsAPI::class.java)

            email = et_user_name.text.toString()
            pswd = et_password.text.toString()
            val userData = LoginRequest(email, pswd)

            request.login(userData)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val loginResponse = response.body()

                        if (loginResponse?.statusCode == 200 && loginResponse?.user != null) {

                            sessionManager.saveAuthToken(loginResponse.authToken)
                            sessionManager.saveUserEmail(loginResponse.user.email)
                            sessionManager.saveUserId(loginResponse.user.id.toString())
                            sessionManager.saveUserName(loginResponse.user.name)
                            sessionManager.saveUserRoleId(loginResponse.user.rola_id.toString())



                            Log.d("LOGIN USER------->>>>", loginResponse.authToken)
                            Log.d("name user------->>>>", User.getInstance().name)


                        } else {

                            Log.d("LOGIN USER------->>>>", "DACO MI TU NEHRAJE")
                            Log.d("LOGIN USER------->>>>", response.body().toString())
                            Log.d("LOGIN USER------->>>>", userData.email)

                            Toast.makeText(
                                this@LoginActivity,
                                "Prihlasenie zlyhalo! zle meno alebo heslo",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                        if (loginResponse?.statusCode == 200 ){
                            reload()
                        }
                    }
                })
        }
    }
    fun reload(){
        Intent(this, MapsActivity::class.java).apply {
            startActivity(this)
        }
    }
}
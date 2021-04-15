package com.bp.digitalizacia_spravy_ciest.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.adapters.UsersListAdapter
import com.bp.digitalizacia_spravy_ciest.models.DeleteAccount
import com.bp.digitalizacia_spravy_ciest.models.EditAccount
import com.bp.digitalizacia_spravy_ciest.models.ShowAllUsers
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import kotlinx.android.synthetic.main.new_report.*
import kotlinx.android.synthetic.main.problems_list.*
import kotlinx.android.synthetic.main.users_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("NAME_SHADOWING")
class UsersListActivity : AppCompatActivity() {

    //  menu staff - Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sessionManager: SessionManager

    lateinit var spinner: Spinner

    var extras : Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.users_list)

        var pocet: Int

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
                        this@UsersListActivity.startActivity(this)
                    }
                    true
                }
                R.id.menuPrihlasenie -> {
                    Intent(this, LoginActivity::class.java).apply {
                        this@UsersListActivity.startActivity(this)
                    }
                    true
                }
                R.id.menuRegistracia -> {
                    Toast.makeText(this, "registracia", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menuOdhlasenie -> {
                    this.sessionManager.clear()
                    Intent(this, MapsActivity::class.java).apply {
                        this@UsersListActivity.startActivity(this)
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

        val context = this

        findViewById<Button>(R.id.odstranitUcetButton).setOnClickListener {
           showdialogDelete()
        }

        findViewById<Button>(R.id.zmenaRoleButton).setOnClickListener {
            showdialogEditRole()
        }


        val request = ServiceBuilder.buildService(CallsAPI::class.java)

        val call = request.getUsers()
        call!!.enqueue(object : Callback<List<ShowAllUsers?>?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<ShowAllUsers?>?>,
                response: Response<List<ShowAllUsers?>?>
            ) {
                if (response.body() != null) {
                    Toast.makeText(
                        this@UsersListActivity,
                        "Zaznamy uspesne zobrazene",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val usersList = response.body()
                pocet = 0

                pocet = usersList?.size!!


                var names = arrayOfNulls<String>(pocet)
                var dates = arrayOfNulls<LocalDate>(pocet)
                var emails = arrayOfNulls<String>(pocet)
                var roles = arrayOfNulls<BigInteger>(pocet)

                if (usersList != null) {
                    var i = 0
                    for (item in usersList) {

                            names[pocet - i - 1] = item!!.name
                            emails[pocet - i - 1] = item.email
                            dates[pocet - i - 1] = LocalDate.parse(item.created_at,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            roles[pocet - i - 1] = item.role.toBigInteger()
                            i += 1
                    }

                }
                val myListAdapter = UsersListAdapter(this@UsersListActivity, names, roles, dates, emails, pocet)
                this@UsersListActivity.userslist.adapter = myListAdapter
            }

            override fun onFailure(call: Call<List<ShowAllUsers?>?>, t: Throwable) {
                Toast.makeText(
                    this@UsersListActivity.applicationContext, t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }


    fun showdialogDelete(){
        var emailtext : String = ""

        Toast.makeText(
            this@UsersListActivity, sessionManager.fetchAuthToken().toString(),
            Toast.LENGTH_SHORT
        ).show()

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setTitle("Vložte e-mail konta, ktoré chcete odstrániť")
        builder.setMessage("Účet bude odstránený na trvalo.")

        val input = EditText(this)
        emailtext = input.text.toString()
        input.setHint("example@example.com")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(R.string.Vymazat){dialogInterface , which ->
            var m_Text = input.text.toString()
            val request = ServiceBuilder.buildService(CallsAPI::class.java)
            val call = request.deleteAccount(
                DeleteAccount(m_Text, sessionManager.fetchAuthToken().toString())
            )
            call.enqueue(object : Callback<Int> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<Int>,
                    response: Response<Int>
                ) {
                    if (response.body() == -1) {
                            val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                            //set title for alert dialog
                            builder.setTitle("UPPS!")
                            //set message for alert dialog
                            builder.setMessage("Zdá sa, že na túto akciu nemáte právo")
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
                    else if (response.body() == 0){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Nesprávny Email")
                        //set message for alert dialog
                        builder.setMessage("Skúste ešte raz a zadajte platný e-mail")
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
                    else if (response.body() == 1){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Účet úspešne vymazaný")
                        //set message for alert dialog
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setNeutralButton("Ok"){dialogInterface , which ->
                            dialogInterface.dismiss()
                            if (m_Text == sessionManager.fetchUserEmail())
                            {
                                restart()
                            }
                            else
                            {
                                refresh()
                            }
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }
                    else if (response.body() == 2){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Tento účet nemôžete odstrániť")
                        //set message for alert dialog
                        builder.setMessage("Účet, ktorý sa chystáte odstrániť je jediný administrátorský. Pre " +
                                "vymazanie, zvolte prosím nového administrátora")
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
                    else if (response.body() == 3){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Tento účet nemôžete odstrániť")
                        //set message for alert dialog
                        builder.setMessage("Účet, ktorý sa chystáte odstrániť je vo vlastníctve administrátora" +
                                ", preto ho nemožno odstrániť")
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
                    else
                    {
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("UPS!")
                        //set message for alert dialog
                        builder.setMessage("Niekde sa stala chyba")
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

        builder.setNegativeButton(R.string.Zrusit, DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        builder.show()
    }

    fun refresh()
    {
        val intent2 = Intent(this, UsersListActivity::class.java)
        this.startActivity(intent2)
    }

    fun showdialogEditRole(){
        var rola : String = ""
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setTitle("Vložte e-mail konta, ktorému chcete zmeniť rolu")
        val input = EditText(this)
        val res: Resources = resources
        val role = arrayOf(res.getStringArray(R.array.role))
        val array = arrayOf("Verejnosť","Dispečer","Manažér","Administrátor")
        builder.setSingleChoiceItems(array,-1) { _, which ->
            rola = array[which]
        }
        input.setHint("example@example.com")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(R.string.ZmenaRole){dialogInterface , which ->
            var vybrane : Int = 0
            if ( rola == "Verejnosť")
                vybrane = 1
            if (rola == "Dispečer")
                vybrane = 4
            if (rola == "Manažér")
                vybrane = 5
            if (rola == "Administrátor")
                vybrane = 3

            val mail = input.text.toString()
            val request = ServiceBuilder.buildService(CallsAPI::class.java)
            val call = request.editAccount(
                EditAccount(mail, vybrane, sessionManager.fetchAuthToken().toString())
            )
            call.enqueue(object : Callback<Int> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<Int>,
                    response: Response<Int>
                ) {
                    if (response.body() == -1) {
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("UPPS!")
                        //set message for alert dialog
                        builder.setMessage("Zdá sa, že na túto akciu nemáte právo")
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
                    else if (response.body() == 0){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Nesprávny Email")
                        //set message for alert dialog
                        builder.setMessage("Skúste ešte raz a zadajte platný e-mail")
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
                    else if (response.body() == 1){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Účet úspešne editovaný")
                        //set message for alert dialog
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        //performing cancel action
                        builder.setNeutralButton("Ok"){dialogInterface , which ->
                            dialogInterface.dismiss()
                            if (mail == sessionManager.fetchUserEmail())
                            {
                                restart()
                            }
                            else
                            {
                                refresh()
                            }
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }

                    else if (response.body() == 2){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Tento účet nemôžete editovať")
                        //set message for alert dialog
                        builder.setMessage("Účet, ktorý sa chystáte editovať je vo vlastníctve administrátora" +
                                ", preto ho nemožno odstrániť")
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
                    else if (response.body() == 3){
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("Tento účet nemôžete editovať")
                        //set message for alert dialog
                        builder.setMessage("Váš účet, ktorý sa chystáte editovať je jediný administrátorsky" +
                                ", preto ho nemožno editovať")
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
                    else
                    {
                        val builder = AlertDialog.Builder(this@UsersListActivity, R.style.AlertDialogTheme)
                        //set title for alert dialog
                        builder.setTitle("UPS!")
                        //set message for alert dialog
                        builder.setMessage("Niekde sa stala chyba")
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

        builder.setNegativeButton(R.string.Zrusit, DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        builder.show()

    }

    fun restart()
    {
            sessionManager.clear()
            Intent(this, MapsActivity::class.java).apply {
                startActivity(this)
            }
    }
}
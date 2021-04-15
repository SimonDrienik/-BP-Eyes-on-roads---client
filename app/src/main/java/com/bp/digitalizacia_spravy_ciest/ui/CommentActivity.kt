package com.bp.digitalizacia_spravy_ciest.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bp.digitalizacia_spravy_ciest.R
import com.bp.digitalizacia_spravy_ciest.models.CommentRequest
import com.bp.digitalizacia_spravy_ciest.server.CallsAPI
import com.bp.digitalizacia_spravy_ciest.server.ServiceBuilder
import com.bp.digitalizacia_spravy_ciest.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {

    var extras : Bundle? = null
    private var idProblem : Int = 0
    private var from : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_comment_activity)

        extras = intent.extras
        if (null != extras) {
            idProblem = extras!!.getInt("problemID")
            from = extras!!.getInt("from")
        }

        findViewById<Button>(R.id.zrusitButton).setOnClickListener {
            Intent(this, DetailActivity::class.java).apply {
                putExtra("idProblem", idProblem)
                putExtra("from", from)
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.potvrditButton).setOnClickListener {7
            val komentText : String = findViewById<EditText>(R.id.komentarText).text.toString()
            if (komentText != "")
            {
                val request = ServiceBuilder.buildService(CallsAPI::class.java)
                var token = ""
                var user = 0
                if (SessionManager(this).fetchAuthToken() == null)
                    token = "n"
                else
                    token = SessionManager(this).fetchAuthToken().toString()

                if (SessionManager(this).fetchUserId() == null)
                    user = 1
                else
                    user = SessionManager(this).fetchUserId()!!.toInt()


                val call = request.comment(CommentRequest(idProblem, komentText, token, user))
                call.enqueue(object : Callback<Int> {
                    override fun onResponse(
                        call: Call<Int>,
                        response: Response<Int>
                    ) {
                        if (response.body() == 1)
                        {
                           refresh()
                        }
                        else
                        {
                            val builder = AlertDialog.Builder(this@CommentActivity, R.style.AlertDialogTheme)
                            //set title for alert dialog
                            builder.setTitle("Hmmmm, Niečo nevyšlo")
                            //set message for alert dialog
                            builder.setMessage("Odosielanie komentáru neprebehlo úspešne... skúste ešte raz"+response.body().toString())
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
            else
            {
                val builder = AlertDialog.Builder(this@CommentActivity, R.style.AlertDialogTheme)
                //set title for alert dialog
                builder.setTitle("Na niečo ste zabudli")
                //set message for alert dialog
                builder.setMessage("Musíte niečo napísať")
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

    }
    fun refresh(){
        finish()
        val intent = Intent(this, MapsActivity::class.java).apply {
            startActivity(this)

        }

    }
}
package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime


class NewReportActivity : AppCompatActivity()  {
     lateinit var editText: EditText
     internal var description = ""
     internal var id = 0
     internal var popisStavuRieseniaProblemu = ""
     internal var stavRieseniaProblemu = ""
     internal var textSelectedStavProblemu = ""
     internal var textSelectedStavVozovky = ""
     @RequiresApi(Build.VERSION_CODES.O)
     internal var current: LocalDateTime = LocalDateTime.now()
     @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_report)
        val button = findViewById<Button>(R.id.button2)
        button?.setOnClickListener()
        {
            val intent = Intent(this, MapsActivity::class.java).apply {
            startActivity(this)
            }
        }

         editText = findViewById(R.id.description)

        val languages = resources.getStringArray(R.array.Languages)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@NewReportActivity,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }

        val stavProblemu = resources.getStringArray(R.array.stav_problemu)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        if (spinner2 != null) {
            val adapter2 = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, stavProblemu
            )
            spinner2.adapter = adapter2

            spinner2.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@NewReportActivity,
                        getString(R.string.selected_item) + " " +
                                "" + stavProblemu[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }



        val button3 = findViewById<Button>(R.id.button3)
        button3?.setOnClickListener()
        {
            val selectedStavVozovky = findViewById(R.id.spinner) as Spinner
            this.textSelectedStavVozovky = spinner.selectedItem.toString()

            val selectedStavProblemu = findViewById(R.id.spinner2) as Spinner
            this.textSelectedStavProblemu = spinner2.selectedItem.toString()

            this.stavRieseniaProblemu = "neuvedene"
            this.popisStavuRieseniaProblemu = "neuvedene"
            this.id = 1
            this.description = editText.text.toString()

            val intent = Intent(this, MapsActivity::class.java).apply {
                startActivity(this)
            }
            val toast: Unit = Toast.makeText(this, "Vyberte na mape miesto dlhym klikom", Toast.LENGTH_LONG).show()

        }

    }
}


package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NewReportActivity : AppCompatActivity()  {

    lateinit var editText: EditText
    internal var description: String = "test"
    internal var popisStavuRieseniaProblemu: String = "test"
    internal var stavRieseniaProblemu: String = "test"
    internal var textSelectedStavProblemu: String = "test"
    internal var id: Int = 0
    internal var textSelectedStavVozovky: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_report)
        val button = findViewById<Button>(R.id.button2)
        button?.setOnClickListener()
        {
             Intent(this, MapsActivity::class.java).apply {
            startActivity(this)
            }
        }

         editText = findViewById(R.id.description)
        val spinner: Spinner = findViewById<Spinner>(R.id.spinner)
        val languages = resources.getStringArray(R.array.Languages)

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
                   /* Toast.makeText(this@NewReportActivity,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT).show()*/
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
                    /*Toast.makeText(this@NewReportActivity,
                        getString(R.string.selected_item) + " " +
                                "" + stavProblemu[position], Toast.LENGTH_SHORT).show()*/
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }



        val button3 = findViewById<Button>(R.id.button3)
        button3?.setOnClickListener()
        {
           id = 1
            // val selectedStavVozovky = findViewById(R.id.spinner) as Spinner
            textSelectedStavVozovky = spinner.selectedItem.toString()
            //val selectedStavProblemu = findViewById(R.id.spinner2) as Spinner
            textSelectedStavProblemu = spinner2.selectedItem.toString()
            //stavRieseniaProblemu = "neuvedene"
            //popisStavuRieseniaProblemu = "neuvedene"

            description = editText.text.toString()
            val toast: Unit = Toast.makeText(this, "Vyberte na mape miesto dlhym klikom", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MapsActivity::class.java)
                //intent.putExtra("id", id)
                intent.putExtra("stav_vozovky", textSelectedStavVozovky)
                intent.putExtra("stav_problemu", textSelectedStavProblemu)
                //intent.putExtra("stav_riesenia_problemu", stavRieseniaProblemu)
                //intent.putExtra("popis_stavu_riesenia_problemu", popisStavuRieseniaProblemu)
                intent.putExtra("description",description)
                startActivity(intent)
        }

    }

}


package com.bp.digitalizacia_spravy_ciest

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.math.BigInteger


class NewReportActivity : AppCompatActivity()  {

    private val WRITE_REQUEST_CODE = 101
    private val READ_REQUEST_CODE = 101

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
        if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_REQUEST_CODE)
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {

                }
            }
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {

                }
            }
        }
    }


    companion object {
        private const val READ_EXTERNAL_STORAGE = 1
    }

    lateinit var editText: EditText
    internal var description: String = "test"
    internal var popisStavuRieseniaProblemu: String = "test"
    internal var stavRieseniaProblemu: String = "test"
    internal var textSelectedStavProblemu: String = "test"
    internal var id: Int = 0
    internal var textSelectedStavVozovky: String = ""
    lateinit var spinner: Spinner
    lateinit var spinner2: Spinner

    lateinit var buttonImg: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var imageView: ImageView
    lateinit var file: File
    lateinit var newID: BigInteger

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_report)
        setupPermissions()
        val button = findViewById<Button>(R.id.button2)
        button?.setOnClickListener()
        {
             Intent(this, MapsActivity::class.java).apply {
            startActivity(this)
            }
        }

        newID = 0.toBigInteger()
        imageView = findViewById(R.id.imageView)

        buttonImg = findViewById(R.id.buttonLoadPicture)
        buttonImg.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

         editText = findViewById(R.id.description)
        spinner = findViewById<Spinner>(R.id.spinner)
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



                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }

        val stavProblemu = resources.getStringArray(R.array.stav_problemu)
        spinner2 = findViewById<Spinner>(R.id.spinner2)
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


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }


        val button3 = findViewById<Button>(R.id.button3)
        button3?.setOnClickListener()
        {

            if (imageUri == null) {
               send()
            }
            else{
                fun getRandomString(length: Int = 10) : String {
                    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
                    return (1..length)
                        .map { charset.random() }
                        .joinToString("")
                }
                val randomString = getRandomString()

                //upload img
                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
                val name = randomString.toRequestBody("text/plain".toMediaTypeOrNull())

                val request = ServiceBuilder.buildService(CallsAPI::class.java)
                val req = request.postImage(body, name)
                /////////////////////////////////////
                req.enqueue(object : Callback<BigInteger> {
                    override fun onResponse(
                        call: Call<BigInteger>,
                        response: Response<BigInteger>
                    ) {
                        if (response.body() != null) {

                            newID = response.body()!!
                            send()
                        }

                    }

                    override fun onFailure(call: Call<BigInteger>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                /////////////////////////////////////
                /*req.enqueue(object:retrofit2.Callback<RequestBody> {
                    override fun onResponse(call: Call<RequestBody>, response: Response<RequestBody>) {
                        if (response.code() > 0) {
                            Toast.makeText(
                                this@NewReportActivity,
                                "Zaznamy uspesne zobrazene",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        newID = response.code().toInt()
                        send()
                    }

                    override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                        Toast.makeText(
                            this@NewReportActivity,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })*/
            }

            

        }

    }

    fun send(){
        id = 1
        // val selectedStavVozovky = findViewById(R.id.spinner) as Spinner
        textSelectedStavVozovky = spinner.selectedItem.toString()
        //val selectedStavProblemu = findViewById(R.id.spinner2) as Spinner
        textSelectedStavProblemu = spinner2.selectedItem.toString()
        //stavRieseniaProblemu = "neuvedene"
        //popisStavuRieseniaProblemu = "neuvedene"

        description = editText.text.toString()
        val toast: Unit =
            Toast.makeText(this, "Vyberte na mape miesto dlhym klikom", Toast.LENGTH_LONG)
                .show()
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("imgID", newID.toInt())
        intent.putExtra("stav_vozovky", textSelectedStavVozovky)
        intent.putExtra("stav_problemu", textSelectedStavProblemu)
        intent.putExtra("description", description)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            val selectedImage: Uri? = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null) ?: return
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            file = File(filePath)
        }
    }

}


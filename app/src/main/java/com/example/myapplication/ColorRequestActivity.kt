package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ColorRequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    //ПЕРЕМ
    var year = 0
    var month = 0
    var day = 0
    var savedYear = 0
    var savedMonth = 0
    var savedDay = 0

    //Объекты

    private lateinit var btnDate: Button
    private lateinit var btnSend: Button
    private lateinit var btnBack: ImageView
    private lateinit var ownerName: TextInputEditText
    private lateinit var phoneNumber: TextInputEditText
    private lateinit var carModel: TextInputEditText
    private lateinit var color: TextInputEditText


    //Код

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_color_request)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnDate = findViewById(R.id.btnDate)
        btnBack = findViewById(R.id.btnBack)
        btnSend = findViewById(R.id.sendBtn)
        ownerName = findViewById(R.id.ownerName)
        phoneNumber = findViewById(R.id.phoneNumber)
        carModel = findViewById(R.id.carModel)
        color = findViewById(R.id.color)

        btnDate.setOnClickListener {
            datePick()
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnSend.setOnClickListener {
            addRequest(btnDate.text.toString(), this)
        }

    }

    private fun addRequest(
        date: String,
        context: Context
    ) {
        val ownerN = ownerName.text.toString()
        val phoneN = phoneNumber.text.toString()
        val carM = carModel.text.toString()
        val col = color.text.toString()
        if (ownerN == "" || phoneN == "" || carM == "" || col == "") {
            Toast.makeText(
                this,
                "Имя, телефон, модель машины, цвет - не может быть пустым полем",
                Toast.LENGTH_LONG
            ).show()
        } else {
            sendRepairRequest(ownerN,phoneN,carM,col,date,context)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendRepairRequest(
        ownerN: String,
        phoneN: String,
        carM: String,
        col: String,
        date: String,
        context: Context
    ) {
        val jsonObject = JSONObject()
        jsonObject.put("ownerName", ownerN)
        jsonObject.put("phoneNumber", phoneN)
        jsonObject.put("carModel", carM)
        jsonObject.put("color",col)
        jsonObject.put("date",date)
        val jsonObjectString = jsonObject.toString()
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.232.139.226:8080/api/painting_requests")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty(
                "Content-Type",
                "application/json"
            ) // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true

            // Send the JSON we created
            val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
            outputStreamWriter.write(jsonObjectString)
            outputStreamWriter.flush()

            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main){
                    AlertDialog.Builder(context)
                        .setTitle("Отправка данных")
                        .setMessage("Данные успешно отправлены")
                        .setCancelable(false)
                        .setPositiveButton("Ок"){dialog, it->
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        }.show()

                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }

    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun datePick() {
        getDateTimeCalendar()
        DatePickerDialog(this, this, year, month, day).show()

    }

    override fun onDateSet(
        view: DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        btnDate.text = "$savedYear-$savedMonth-$savedDay"
    }
}
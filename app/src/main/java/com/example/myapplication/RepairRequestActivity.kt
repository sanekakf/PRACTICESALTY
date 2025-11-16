package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

class RepairRequestActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    //ПЕРЕМЕННЫЕ
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    //Объекты
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnBack: ImageView
    private lateinit var btnSend: Button
    private lateinit var name: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var carModel: TextInputEditText
    private lateinit var issueDecription: TextInputEditText
    private lateinit var builder: AlertDialog.Builder


    //СОЗДАНИЕ СТРАНИЦЫ ЕБУЧЕЙ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair_request)

        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)
        btnBack = findViewById(R.id.btnBack)
        btnSend = findViewById(R.id.btnSend)

        builder = AlertDialog.Builder(this)

        name = findViewById(R.id.name)
        phone = findViewById(R.id.phone)
        carModel = findViewById(R.id.carModel)
        issueDecription = findViewById(R.id.issueDescription)


        //Обработка событий
        btnDate.setOnClickListener {
            pickDate()
        }
        btnTime.setOnClickListener {
            pickTime()
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnSend.setOnClickListener {
            addRequest(btnDate.text.toString(), btnTime.text.toString(), this)
        }


    }

    private fun addRequest(date: String, time: String, context: Context) {
        val namet = name.text.toString()
        val phonet = phone.text.toString()
        val carMt = carModel.text.toString()
        val issueDt = issueDecription.text.toString()
        if (namet == "" || phonet == "" || carMt == "") {
            Toast.makeText(
                this,
                "Имя, телефон, модель машины - не может быть пустым полем",
                Toast.LENGTH_LONG
            ).show()
        } else {
            sendRepairRequest(namet,phonet,carMt,issueDt,date,time, context)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendRepairRequest(
        ownerName: String,
        phoneNumber: String,
        carModel: String,
        issueD: String,
        date: String,
        time: String,
        context: Context
    ) {

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("ownerName", ownerName)
        jsonObject.put("phoneNumber", phoneNumber)
        jsonObject.put("carModel", carModel)
        jsonObject.put("issueDescription",issueD)
        jsonObject.put("date",date)
        jsonObject.put("time",time)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://95.31.5.158:8080/api/repair_requests")
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
                    val builder  = AlertDialog.Builder(context)
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
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        getDateTimeCalendar()

        DatePickerDialog(this, this, year, month, day).show()

    }

    private fun pickTime() {
        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(
        view: TimePicker?,
        hourOfDay: Int,
        minute: Int
    ) {
        savedHour = hourOfDay
        savedMinute = minute
        btnTime.text = "$savedHour-$savedMinute"
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
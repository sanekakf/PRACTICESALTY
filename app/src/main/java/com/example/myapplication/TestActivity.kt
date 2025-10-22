package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TestActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
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


    //СОЗДАНИЕ СТРАНИЦЫ ЕБУЧЕЙ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btnDate = findViewById(R.id.btPicker)
        btnTime = findViewById(R.id.btnTime)
        btnBack = findViewById<ImageView>(R.id.btnBack)

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
        btnTime.text = "Время: Часы-$savedHour Минуты-$savedMinute"
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
        btnDate.text = "Дата - $savedYear/$savedMonth/$savedDay"
    }

}
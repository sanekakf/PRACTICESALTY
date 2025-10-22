package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Color : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    //ПЕРЕМ
    var year = 0
    var month = 0
    var day = 0
    var savedYear = 0
    var savedMonth = 0
    var savedDay = 0

    //Объекты

    private lateinit var btnDate: Button
    private lateinit var btnBack: ImageView


    //Код

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_color)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnDate = findViewById<Button>(R.id.btnDate)
        btnBack = findViewById<ImageView>(R.id.btnBack)

        btnDate.setOnClickListener {
            datePick()
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
        btnDate.text = "Дата - $savedYear/$savedMonth/$savedDay"
    }
}
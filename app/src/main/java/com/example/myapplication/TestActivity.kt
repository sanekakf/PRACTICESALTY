package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.util.Locale

class TestActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    //ПЕРЕМЕННЫЕ
    var day =0
    var month =0
    var year =0
    var hour = 0
    var minute =0

    var savedDay =0
    var savedMonth =0
    var savedYear =0
    var savedHour =0
    var savedMinute =0

    //Объекты
    private lateinit var dateExp: TextView
    private lateinit var timeExp: TextView
    private lateinit var btnDate: Button


    //СОЗДАНИЕ СТРАНИЦЫ ЕБУЧЕЙ
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        dateExp = findViewById(R.id.btExport)
        timeExp = findViewById(R.id.btExport2)
        btnDate = findViewById(R.id.btPicker)




        btnDate.setOnClickListener {
            pickDate()
        }

    }
    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        getDateTimeCalendar()

        DatePickerDialog(this, this, year,month,day).show()

    }

    private fun pickTime() {
        TODO("Not yet implemented")
    }

    override fun onTimeSet(
        view: TimePicker?,
        hourOfDay: Int,
        minute: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onDateSet(
        view: DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        savedDay = dayOfMonth
        savedMonth = month+1
        savedYear = year
        dateExp.text = "$savedDay-$savedMonth-$savedYear"


    }

}
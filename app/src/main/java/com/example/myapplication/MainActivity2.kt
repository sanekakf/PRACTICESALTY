package com.example.myapplication

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData

class MainActivity2 : AppCompatActivity() {
    private lateinit var ggextView: TextView
    private lateinit var btPicker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ggextView = findViewById(R.id.btExport)
        btPicker = findViewById(R.id.btPicker)

        btPicker.setOnClickListener {

            showdatepicker()
        }
    }
    private fun showdatepicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var startDate = Calendar.getInstance()
        val startDateListener = MutableLiveData<String>()
    }
}
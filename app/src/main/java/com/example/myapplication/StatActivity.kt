package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import com.example.myapplication.models.repair_request
import com.example.myapplication.models.painting_request
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.component1
import kotlin.collections.component2
import androidx.core.graphics.toColorInt

class StatActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var allRepairCount: TextView
    private lateinit var allPaintingCount: TextView
    private lateinit var workRepairCount: TextView
    private lateinit var workPaintingCount: TextView
    private lateinit var doneRepairCount: TextView
    private lateinit var donePaintingCount: TextView
    private lateinit var linearRepairLayout: LinearLayout
    private lateinit var linearPaintingLayout: LinearLayout


    fun parseRepairOrders(jsonString: String): Map<String, repair_request> {/*
        * Парсинг Запросов на ремонт
        * */
        val type = object : TypeToken<Map<String, repair_request>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    fun parsePaintingOrders(jsonString: String): Map<String, painting_request> {/*
    *Парсинг запросов на покраску
    */
        val type = object : TypeToken<Map<String, painting_request>>() {}.type
        return Gson().fromJson(jsonString, type)
    }


    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("UnsafeIntentLaunch", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBack = findViewById(R.id.btnBack)
        linearRepairLayout = findViewById(R.id.repairLinearLayout)
        linearPaintingLayout = findViewById(R.id.paintingLinearLayout)
        allRepairCount = findViewById(R.id.allRepairCount)
        allPaintingCount = findViewById(R.id.allPaintCount)
        workRepairCount = findViewById(R.id.workRepairCount)
        workPaintingCount = findViewById(R.id.workPaintCount)
        doneRepairCount = findViewById(R.id.doneRepairCount)
        donePaintingCount = findViewById(R.id.donePaintCount)

        var workRemReq = 0
        var doneRemReq = 0
        var workPaintReq = 0
        var donePaintReq = 0
        //REPAIR TABLE
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.232.139.226:8080/api/repair_requests")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty(
                // The format of response we want to get from the server
                "Accept", "application/json"
            )
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
                    var i = 1

                    val ordersMap = parseRepairOrders(response)
                    ordersMap.forEach { (key, order) ->
                        val repairLayout = LayoutInflater.from(linearRepairLayout.context)
                            .inflate(R.layout.stat_repair_layout, null) as ConstraintLayout
                        val name: TextView = repairLayout.findViewById(R.id.name)
                        val dateCreation: TextView = repairLayout.findViewById(R.id.dateCreation)
                        val timeCreation: TextView = repairLayout.findViewById(R.id.color)
                        val dateComplete: TextView = repairLayout.findViewById(R.id.dateComplete)
                        val timeComplete: TextView = repairLayout.findViewById(R.id.timeComplete)
                        val status: TextView = repairLayout.findViewById(R.id.status)
                        val number: TextView = repairLayout.findViewById(R.id.number)

                        if (order.status.toString() == "в работе") {
                            workRemReq = workRemReq + 1
                            println(workRemReq)
                        } else {
                            doneRemReq = doneRemReq + 1
                            println(doneRemReq)
                        }

                        number.text = i.toString()
                        name.text = order.ownerName
                        dateCreation.text = "Дата создания: ${order.date}"
                        timeCreation.text = "Время создания: ${order.time}"
                        dateComplete.text = "Дата выполнения: ${order.completionDate}"
                        timeComplete.text = "Время выполнения: ${order.completionTime}"
                        status.text = order.status
                        if (order.status.toString() == "выполнено") {
                            status.setTextColor("#60BF60".toColorInt())
                        } else if (order.status.toString() == "в работе") {
                            status.setTextColor("#FF5722".toColorInt())
                        } else {
                            status.setTextColor("#FF0000".toColorInt())
                        }
                        repairLayout.id = order.id

                        i = i + 1
                        linearRepairLayout.addView(repairLayout)

                    }
                    workRepairCount.text = "Заявки на ремонт в работе: ${workRemReq.toString()}"
                    doneRepairCount.text = "Заявки на ремонт выполнены: ${doneRemReq.toString()}"
                    allRepairCount.text =
                        "Общее количество заявок на ремонт: ${(workRemReq + doneRemReq).toString()}"

                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }

        }


        //PAINTING TABLE
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.232.139.226:8080/api/painting_requests")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty(
                // The format of response we want to get from the server
                "Accept", "application/json"
            )
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
                    var i = 1

                    val ordersMap = parsePaintingOrders(response)
                    ordersMap.forEach { (key, order) ->
                        val paintingLayout = LayoutInflater.from(linearPaintingLayout.context)
                            .inflate(R.layout.stat_painting_layout, null) as ConstraintLayout
                        val name: TextView = paintingLayout.findViewById(R.id.name)
                        val dateCreation: TextView = paintingLayout.findViewById(R.id.dateCreation)
                        val color: TextView = paintingLayout.findViewById(R.id.color)
                        val status: TextView = paintingLayout.findViewById(R.id.status)
                        val number: TextView = paintingLayout.findViewById(R.id.number)

                        if (order.status.toString() == "в работе") {
                            workPaintReq = workRemReq + 1
                        } else {
                            donePaintReq = doneRemReq + 1
                            println(doneRemReq)
                        }

                        number.text = i.toString()
                        name.text = order.ownerName
                        dateCreation.text = "Дата создания: ${order.date}"
                        color.text = "Цвет: ${order.color}"
                        status.text = order.status
                        if (order.status.toString() == "выполнено") {
                            status.setTextColor("#60BF60".toColorInt())
                        } else if (order.status.toString() == "в работе") {
                            status.setTextColor("#FF5722".toColorInt())
                        } else {
                            status.setTextColor("#FF0000".toColorInt())
                        }
                        paintingLayout.id = order.id

                        i = i + 1
                        linearPaintingLayout.addView(paintingLayout)

                    }
                    workPaintingCount.text = "Заявки на покраску в работе: ${workPaintReq.toString()}"
                    donePaintingCount.text = "Заявки на покраску выполнены: ${donePaintReq.toString()}"
                    allPaintingCount.text =
                        "Общее количество заявок на покраску: ${(workPaintReq + donePaintReq).toString()}"

                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }

        }





        btnBack.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
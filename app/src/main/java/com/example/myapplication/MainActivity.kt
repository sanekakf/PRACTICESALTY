package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {
    private lateinit var btnRem: Button
    private lateinit var btnStat: Button
    private lateinit var btnColor: Button
    private lateinit var tableRepair: TableLayout
    private lateinit var tablePainting: TableLayout

    fun parseRepairOrders(jsonString: String): Map<String, repair_request>{
        /*
        *
        * */
        val type = object: TypeToken<Map<String, repair_request>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    fun parsePaintingOrders(jsonString: String): Map<String, painting_request>{
        val type = object: TypeToken<Map<String, painting_request>>(){}.type
        return Gson().fromJson(jsonString,type)
    }

    fun delete ( id: Int, type: String){
        println(id)
        println(type)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRem = findViewById(R.id.btnRem)
        btnStat = findViewById(R.id.btnStat)
        btnColor = findViewById(R.id.btnColor)
        tableRepair = findViewById(R.id.tableRepair)
        tablePainting = findViewById(R.id.tablePainting)


        //PARSE FROM API MY  FCKNG SERVER

        //repair table
        GlobalScope.launch(Dispatchers.IO) {
                val url = URL("http://77.232.139.226:8080/api/repair_requests")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.doInput = true
                httpURLConnection.doOutput = false
                // Check if the connection is successful
                val responseCode = httpURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = httpURLConnection.inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
                    withContext(Dispatchers.Main) {

                        val ordersMap = parseRepairOrders(response)
                        ordersMap.forEach { (key, order) ->
                            val tableRow = LayoutInflater.from(tableRepair.context).inflate(R.layout.repair_table_row, null) as TableRow
                            val rowPhone : TextView = tableRow.findViewById(R.id.Phone)
                            val rowModel : TextView = tableRow.findViewById(R.id.Model)
                            val rowDate : TextView = tableRow.findViewById(R.id.Date)
                            val rowTime : TextView = tableRow.findViewById(R.id.Time)
                            val rowStatus : TextView = tableRow.findViewById(R.id.Status)

                            val editBtn : ImageView = tableRow.findViewById(R.id.Edit)
                            val doneBtn : ImageView = tableRow.findViewById(R.id.Done)
                            val delBtn : ImageView = tableRow.findViewById(R.id.Delete)

                            delBtn.setOnClickListener {
                                delete(order.id, "repair")
                            }

                            tableRow.id = order.id
                            rowPhone.text = order.phoneNumber
                            rowModel.text = order.carModel
                            rowDate.text = order.date
                            rowTime.text = order.time
                            rowStatus.text = order.status



                            tableRepair.addView(tableRow)

                        }



                    }
                } else {
                    Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
                }
            }


        //painting table
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://77.232.139.226:8080/api/painting_requests")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    val ordersMap = parsePaintingOrders(response)
                    ordersMap.forEach { (key, order) ->
                        val tableRow = LayoutInflater.from(tablePainting.context).inflate(R.layout.painting_table_row, null) as TableRow
                        val rowModel : TextView = tableRow.findViewById(R.id.Model)
                        val rowColor : TextView = tableRow.findViewById(R.id.Color)
                        val rowDate : TextView = tableRow.findViewById(R.id.Date)
                        val rowStatus : TextView = tableRow.findViewById(R.id.Status)

                        val editBtn : ImageView = tableRow.findViewById(R.id.Edit)
                        val doneBtn : ImageView = tableRow.findViewById(R.id.Done)
                        val delBtn: ImageView = tableRow.findViewById(R.id.Delete)

                        delBtn.setOnClickListener {
                            delete(order.id, "painting")
                        }

                        tableRow.id = order.id
                        rowModel.text = order.carModel
                        rowColor.text = order.color
                        rowDate.text = order.date
                        rowStatus.text = order.status


                        tablePainting.addView(tableRow)

                    }



                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }

        //
        btnRem.setOnClickListener {
            val intent = Intent(this, RepairRequestActivity::class.java)
            startActivity(intent)
        }
        btnStat.setOnClickListener {
            val intent = Intent(this, StatActivity::class.java)
            startActivity(intent)
        }
        btnColor.setOnClickListener {
            val intent = Intent(this, ColorRequestActivity::class.java)
            startActivity(intent)
        }

    }
}

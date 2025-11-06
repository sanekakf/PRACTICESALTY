package com.example.myapplication.models

data class repair_request(
    val id : Int,
    val ownerName : String,
    val phoneNumber : String,
    val carModel : String,
    val issueDescription : String,
    val date : String,
    val time : String,
    val status : String,
    val completionTime : String,
    val completionDate : String

)

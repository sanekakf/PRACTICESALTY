package com.example.myapplication.models

data class painting_request(
    val id : Int,
    val ownerName: String,
    val phoneNumber: String,
    val carModel: String,
    val color: String,
    val date: String,
    val status: String
)

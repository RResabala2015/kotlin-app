package com.example.dabf.model

import com.google.gson.annotations.SerializedName

data class Ordenes(
    val id: Int,
    val traking: String,
    val courier:String,
    @SerializedName("total_price") val totalPrice:String,
    val status: String) {
}
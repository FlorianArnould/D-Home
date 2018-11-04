package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

class Data: ErrorResponse() {
    @SerializedName("data")
    val data: String = ""
    @SerializedName("isFinished")
    val isFinished: Boolean = false
}
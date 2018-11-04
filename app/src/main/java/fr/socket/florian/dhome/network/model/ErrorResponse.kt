package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

open class ErrorResponse {
    @SerializedName("error")
    val error: String = ""
}
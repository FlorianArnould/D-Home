package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

class Stream : ErrorResponse() {
    @SerializedName("id")
    val id: String = ""
}
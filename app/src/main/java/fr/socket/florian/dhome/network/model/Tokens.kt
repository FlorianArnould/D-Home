package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

class Tokens : ResponseBase() {

    @SerializedName("sessionToken")
    val sessionToken: String? = null

    @SerializedName("refreshToken")
    val refreshToken: String? = null
}

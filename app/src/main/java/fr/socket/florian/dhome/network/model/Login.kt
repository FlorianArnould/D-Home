package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

class Login : ResponseBase() {

    @SerializedName("refreshToken")
    val refreshToken: String? = null

    @SerializedName("sessionToken")
    val sessionToken: String? = null
}

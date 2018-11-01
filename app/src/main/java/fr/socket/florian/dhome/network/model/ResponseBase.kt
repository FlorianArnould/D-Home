package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

open class ResponseBase {

    @SerializedName("auth")
    val isAuth: Boolean = false

    @SerializedName("message")
    val message: String? = null

}

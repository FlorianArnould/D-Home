package fr.socket.florian.dhome.network.model

import com.google.gson.annotations.SerializedName

class Device {
    @SerializedName("id")
    val id: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("description")
    val description: String? = null

    @SerializedName("type")
    val type: Int = 0
}

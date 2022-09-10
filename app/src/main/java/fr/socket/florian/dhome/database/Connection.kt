package fr.socket.florian.dhome.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Connection(val serverUrl: String, val username: String, var refreshToken: String, var sessionToken: String, val id: Int = 0) : Parcelable

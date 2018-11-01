package fr.socket.florian.dhome.database

class Connection(val serverUrl: String, val username: String, var refreshToken: String, var sessionToken: String, val id: Int = 0)

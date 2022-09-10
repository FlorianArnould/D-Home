package fr.socket.florian.dhome.network.model

enum class Status {
    ON, OFF;

    override fun toString(): String {
        return name.toLowerCase()
    }
}
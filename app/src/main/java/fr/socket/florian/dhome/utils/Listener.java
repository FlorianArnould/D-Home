package fr.socket.florian.dhome.utils;

public interface Listener<T> {
    void callback(T answer);
}

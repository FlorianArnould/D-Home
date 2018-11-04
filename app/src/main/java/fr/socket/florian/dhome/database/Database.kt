package fr.socket.florian.dhome.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import java.io.Closeable
import java.util.*

class Database(context: Context) : Closeable {
    private val db: SQLiteDatabase = DatabaseOpenHelper(context).writableDatabase

    fun getConnectionById(id: Int, callback: (Connection) -> Unit) {
        GetConnectionByIdAsyncTask(db, id, callback).execute()
    }

    fun getConnections(callback: (List<Connection>) -> Unit) {
        GetConnectionsAsyncTask(db, callback).execute()
    }

    fun addConnection(connection: Connection) {
        AddConnectionAsyncTask(db, connection).execute()
    }

    fun updateConnection(connection: Connection) {
        UpdateConnectionAsyncTask(db, connection).execute()
    }

    override fun close() {
        db.close()
    }

    private class GetConnectionsAsyncTask(private val db: SQLiteDatabase, private val callback: (List<Connection>) -> Unit) : AsyncTask<Void, Void, List<Connection>>() {

        override fun doInBackground(vararg voids: Void): List<Connection> {
            val list = ArrayList<Connection>()
            db.query(CONNECTIONS_TABLE, null, null, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        list.add(Connection(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(0)))
                    } while (cursor.moveToNext())
                }
            }
            return list
        }

        override fun onPostExecute(result: List<Connection>) {
            callback(result)
        }
    }

    private class AddConnectionAsyncTask(private val db: SQLiteDatabase, private val connection: Connection) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            db.insert(CONNECTIONS_TABLE, null, Database.toContentValues(connection))
            return null
        }
    }

    private class GetConnectionByIdAsyncTask(private val db: SQLiteDatabase, private val id: Int, private val callback: (Connection) -> Unit) : AsyncTask<Void, Void, Connection>() {

        override fun doInBackground(vararg voids: Void): Connection? {
            db.query(CONNECTIONS_TABLE, null, "id = ?", arrayOf(id.toString()), null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    return Connection(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(0))
                }
            }
            return null
        }

        override fun onPostExecute(result: Connection) {
            callback(result)
        }
    }

    private class UpdateConnectionAsyncTask(private val db: SQLiteDatabase, private val connection: Connection) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            db.update(CONNECTIONS_TABLE, Database.toContentValues(connection), "id = ?", arrayOf(connection.id.toString()))
            return null
        }
    }

    companion object {
        private const val CONNECTIONS_TABLE = "connections"

        private fun toContentValues(connection: Connection): ContentValues {
            val values = ContentValues()
            values.put("url", connection.serverUrl)
            values.put("username", connection.username)
            values.put("refresh_token", connection.refreshToken)
            values.put("session_token", connection.sessionToken)
            return values
        }
    }
}

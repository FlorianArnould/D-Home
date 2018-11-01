package fr.socket.florian.dhome.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS connections (\n" +
                "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\turl TEXT NOT NULL,\n" +
                "\tusername TEXT NOT NULL,\n" +
                "\trefresh_token TEXT NOT NULL,\n" +
                "\tsession_token TEXT NOT NULL);")
    }

    override fun onUpgrade(db: SQLiteDatabase, from: Int, to: Int) {
        if (from != to) {
            db.execSQL("DROP TABLE connections;")
            onCreate(db)
        }
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "connections.db"
    }
}

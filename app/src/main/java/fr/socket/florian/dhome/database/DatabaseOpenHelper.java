package fr.socket.florian.dhome.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "connections.db";

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS connections (\n" +
                "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\turl TEXT NOT NULL,\n" +
                "\tusername TEXT NOT NULL,\n" +
                "\trefresh_token TEXT NOT NULL,\n" +
                "\tsession_token TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        if (from != to) {
            db.execSQL("DROP TABLE connections;");
            onCreate(db);
        }
    }
}

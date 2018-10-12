package fr.socket.florian.dhome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class Database implements Closeable {
    private final SQLiteDatabase db;

    public Database(Context context) {
        db = new DatabaseOpenHelper(context).getWritableDatabase();
    }

    private List<Connection> getConnections() {
        List<Connection> list = new ArrayList<>();
        try (Cursor cursor = db.query("connections", new String[]{"url", "username", "refresh_token", "session_token"}, null, null, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(new Connection(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                    } while (cursor.moveToNext());
                }
            }
        }
        return list;
    }

    public void getConnections(@NonNull Callback<List<Connection>> callback) {
        new GetConnectionsAsyncTask(this, callback).execute();
    }

    private void privateAddConnection(Connection connection) {
        ContentValues values = new ContentValues();
        values.put("url", connection.getServerUrl());
        values.put("username", connection.getUsername());
        values.put("refresh_token", connection.getRefreshToken());
        values.put("session_token", connection.getSessionToken());
        db.insert("connections", null, values);
    }

    public void addConnection(Connection connection) {
        new AddConnectionAsyncTask(this, connection).execute();
    }

    @Override
    public void close() {
        db.close();
    }

    public interface Callback<T> {
        void callback(T answer);
    }

    private static class GetConnectionsAsyncTask extends AsyncTask<Void, Void, List<Connection>> {
        private final Database db;
        private final Callback<List<Connection>> callback;

        GetConnectionsAsyncTask(@NonNull Database db, @NonNull Callback<List<Connection>> callback) {
            this.db = db;
            this.callback = callback;
        }

        @Override
        protected List<Connection> doInBackground(Void... voids) {
            return db.getConnections();
        }

        @Override
        protected void onPostExecute(List<Connection> result) {
            callback.callback(result);
        }
    }

    private static class AddConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Database db;
        private final Connection connection;

        AddConnectionAsyncTask(Database db, Connection connection) {
            this.db = db;
            this.connection = connection;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.privateAddConnection(connection);
            return null;
        }
    }
}
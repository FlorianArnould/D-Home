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

import fr.socket.florian.dhome.utils.Listener;

public class Database implements Closeable {
    private static final String CONNECTIONS_TABLE = "connections";
    private final SQLiteDatabase db;

    public Database(Context context) {
        db = new DatabaseOpenHelper(context).getWritableDatabase();
    }

    public void getConnectionById(int id, @NonNull Listener<Connection> listener) {
        new GetConnectionByIdAsyncTask(db, id, listener).execute();
    }

    public void getConnections(@NonNull Listener<List<Connection>> listener) {
        new GetConnectionsAsyncTask(db, listener).execute();
    }

    public void addConnection(Connection connection) {
        new AddConnectionAsyncTask(db, connection).execute();
    }

    public void updateConnection(Connection connection) {
        new UpdateConnectionAsyncTask(db, connection).execute();
    }

    @Override
    public void close() {
        db.close();
    }

    private static class GetConnectionsAsyncTask extends AsyncTask<Void, Void, List<Connection>> {
        private final SQLiteDatabase db;
        private final Listener<List<Connection>> listener;

        GetConnectionsAsyncTask(@NonNull SQLiteDatabase db, @NonNull Listener<List<Connection>> listener) {
            this.db = db;
            this.listener = listener;
        }

        @Override
        protected List<Connection> doInBackground(Void... voids) {
            List<Connection> list = new ArrayList<>();
            try (Cursor cursor = db.query(CONNECTIONS_TABLE, null, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        list.add(new Connection(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                    } while (cursor.moveToNext());
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Connection> result) {
            listener.callback(result);
        }
    }

    private static class AddConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private final SQLiteDatabase db;
        private final Connection connection;

        AddConnectionAsyncTask(@NonNull SQLiteDatabase db, @NonNull Connection connection) {
            this.db = db;
            this.connection = connection;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.insert(CONNECTIONS_TABLE, null, Database.toContentValues(connection));
            return null;
        }
    }

    private static class GetConnectionByIdAsyncTask extends AsyncTask<Void, Void, Connection> {
        private final SQLiteDatabase db;
        private final int id;
        private final Listener<Connection> listener;

        GetConnectionByIdAsyncTask(@NonNull SQLiteDatabase db, int id, @NonNull Listener<Connection> listener) {
            this.db = db;
            this.id = id;
            this.listener = listener;
        }

        @Override
        protected Connection doInBackground(Void... voids) {
            try (Cursor cursor = db.query(CONNECTIONS_TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    return new Connection(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Connection result) {
            listener.callback(result);
        }
    }

    private static class UpdateConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private final SQLiteDatabase db;
        private final Connection connection;

        UpdateConnectionAsyncTask(@NonNull SQLiteDatabase db, @NonNull Connection connection) {
            this.db = db;
            this.connection = connection;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.update(CONNECTIONS_TABLE, Database.toContentValues(connection), "id = ?", new String[]{String.valueOf(connection.getId())});
            return null;
        }
    }

    private static ContentValues toContentValues(Connection connection) {
        ContentValues values = new ContentValues();
        values.put("url", connection.getServerUrl());
        values.put("username", connection.getUsername());
        values.put("refresh_token", connection.getRefreshToken());
        values.put("session_token", connection.getSessionToken());
        return values;
    }
}

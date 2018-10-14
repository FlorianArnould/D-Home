package fr.socket.florian.dhome.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Closeable;
import java.util.List;

import fr.socket.florian.dhome.database.Connection;
import fr.socket.florian.dhome.database.Database;
import fr.socket.florian.dhome.network.model.Device;
import fr.socket.florian.dhome.network.model.Tokens;
import fr.socket.florian.dhome.utils.Listener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager implements Closeable {

    private Connection connection;
    private Network network;
    private Database db;

    public void initialize(final Context context, int connectionId, final Listener<ApiManager> listener) {
        db = new Database(context);
        db.getConnectionById(connectionId, new Listener<Connection>() {
            @Override
            public void callback(Connection connection) {
                ApiManager.this.connection = connection;
                network = new ApiModule("https://" + connection.getServerUrl(), context).provideApi();
                listener.callback(ApiManager.this);
            }
        });
    }

    public void getAllDevices(final Listener<List<Device>> listener) {
        network.allDevices(connection.getSessionToken()).enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(@NonNull Call<List<Device>> call, @NonNull Response<List<Device>> response) {
                if (response.isSuccessful()) {
                    listener.callback(response.body());
                } else {
                    // TODO: 14/10/18 parse the errorBody
                    refreshSessionToken(new Listener<Void>() {
                        @Override
                        public void callback(Void answer) {
                            getAllDevices(listener);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Device>> call, @NonNull Throwable t) {
                Log.e("ApiManager getAllDevices", "Got Failure : " + t.getMessage(), t);
            }
        });
    }

    private void refreshSessionToken(final Listener<Void> listener) {
        Log.i("ApiManager", "refreshing session token");
        network.refreshToken(connection.getRefreshToken()).enqueue(new Callback<Tokens>() {
            @Override
            public void onResponse(@NonNull Call<Tokens> call, @NonNull Response<Tokens> response) {
                if (response.isSuccessful() && response.body() != null) {
                    connection.setSessionToken(response.body().getSessionToken());
                    String refreshToken = response.body().getRefreshToken();
                    if (refreshToken != null) {
                        connection.setRefreshToken(refreshToken);
                    }
                    db.updateConnection(connection);
                    listener.callback(null);
                } else {
                    Log.e("ApiManager refreshToken", "Got an error : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tokens> call, @NonNull Throwable t) {
                Log.e("ApiManager refreshToken", "Got Failure : " + t.getMessage(), t);
            }
        });
    }

    @Override
    public void close() {
        if (db != null) {
            db.close();
        }
    }
}

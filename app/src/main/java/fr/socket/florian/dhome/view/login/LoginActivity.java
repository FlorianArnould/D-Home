package fr.socket.florian.dhome.view.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.database.Connection;
import fr.socket.florian.dhome.database.Database;
import fr.socket.florian.dhome.network.ApiModule;
import fr.socket.florian.dhome.network.Network;
import fr.socket.florian.dhome.network.model.CheckServer;
import fr.socket.florian.dhome.network.model.Login;
import fr.socket.florian.dhome.utils.Listener;
import fr.socket.florian.dhome.view.MainActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, View.OnFocusChangeListener, Callback<Login> {

    private static final String CHECK_SERVER_LOG_TAG = "Network check server";
    private static final String LOGIN_LOG_TAG = "Network login";

    private TextView errorText;
    private Database db;
    private ProgressBar serverCheckProgress;
    private ImageView serverCheckImage;
    private Button button;
    private ProgressBar progress;
    private TextView text;
    private EditText serverUrl;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));

        final ContainerViewHandler rootLayout = findViewById(R.id.root_layout);
        rootLayout.setKeyboardStateListener(new ContainerViewHandler.OnKeyboardStateChange() {
            @Override
            public void onKeyboardShow() {
                TransitionManager.beginDelayedTransition(rootLayout);
            }

            @Override
            public void onKeyboardHide() {
                TransitionManager.beginDelayedTransition(rootLayout);
            }
        });

        db = new Database(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getConnections(new Listener<List<Connection>>() {
                    @Override
                    public void callback(List<Connection> answer) {
                        if (answer.isEmpty()) {
                            View formLayout = findViewById(R.id.form_layout);
                            TransitionManager.beginDelayedTransition(rootLayout);
                            ConstraintSet set = new ConstraintSet();
                            set.clone(LoginActivity.this, R.layout.activity_login_transition);
                            set.applyTo(rootLayout);
                            formLayout.setVisibility(View.VISIBLE);
                            formLayout.animate().alpha(1).setDuration(500);
                        } else {
                            startMainActivity();
                        }
                    }
                });
            }
        }, 1000);

        username = findViewById(R.id.edit_username);

        serverCheckProgress = findViewById(R.id.server_check_progress);
        serverCheckImage = findViewById(R.id.server_check_image);

        progress = findViewById(R.id.progress);
        text = findViewById(R.id.button_text);
        errorText = findViewById(R.id.error_text);

        serverUrl = findViewById(R.id.edit_server_url);
        serverUrl.setOnFocusChangeListener(this);

        EditText editPassword = findViewById(R.id.edit_password);
        editPassword.setOnEditorActionListener(this);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    private void displayButtonToProgress(final Runnable runnable) {
        button.setEnabled(false);
        text.animate().alpha(0).setDuration(500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.animate().alpha(1).setDuration(500);
                runnable.run();
            }
        }, 500);
    }

    private void animateProgressToSuccess(final Runnable runnable) {
        progress.animate().alpha(0).setDuration(500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{getDrawable(R.drawable.login_button_normal), getDrawable(R.drawable.login_button_success)});
                button.setBackground(transitionDrawable);
                transitionDrawable.setCrossFadeEnabled(true);
                transitionDrawable.startTransition(500);
                ImageView resultIcon = findViewById(R.id.result_icon);
                resultIcon.setImageResource(R.drawable.ic_check);
                resultIcon.animate().alpha(1).setDuration(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runnable.run();
                    }
                }, 500);
            }
        }, 500);
    }

    private void animateProgressToError() {
        progress.animate().alpha(0).setDuration(500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{getDrawable(R.drawable.login_button_normal), getDrawable(R.drawable.login_button_error)});
                button.setBackground(transitionDrawable);
                transitionDrawable.setCrossFadeEnabled(true);
                transitionDrawable.startTransition(500);
                final ImageView resultIcon = findViewById(R.id.result_icon);
                resultIcon.setImageResource(R.drawable.ic_close);
                resultIcon.animate().alpha(1).setDuration(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resultIcon.animate().alpha(0).setDuration(500);
                        transitionDrawable.reverseTransition(500);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text.animate().alpha(1).setDuration(500);
                                button.setEnabled(true);
                            }
                        }, 500);
                    }
                }, 1000);
            }
        }, 500);
    }

    private void displayServerCheckProgressBar() {
        if (serverCheckImage.getAlpha() > 0) {
            serverCheckImage.animate().alpha(0).setDuration(500);
        }
        serverCheckProgress.animate().alpha(1).setDuration(500);
    }

    private void displayServerCheckResult(final boolean succeed) {
        serverCheckProgress.animate().alpha(0).setDuration(500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (succeed) {
                    serverCheckImage.setColorFilter(getColor(R.color.green));
                    serverCheckImage.setImageResource(R.drawable.ic_check);
                } else {
                    serverCheckImage.setColorFilter(getColor(R.color.red));
                    serverCheckImage.setImageResource(R.drawable.ic_close);
                }
                serverCheckImage.animate().alpha(1).setDuration(500);
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
            button.performClick();
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            String serverUrlString = serverUrl.getText().toString();
            if (serverUrlString.isEmpty()) {
                displayServerCheckResult(false);
                return;
            }
            displayServerCheckProgressBar();
            Network network = new ApiModule("https://" + serverUrlString, LoginActivity.this)
                    .provideApi();
            network.checkServer().enqueue(new Callback<CheckServer>() {
                @Override
                public void onResponse(@NonNull Call<CheckServer> call, @NonNull Response<CheckServer> response) {
                    if (response.body() != null) {
                        displayServerCheckResult(response.body().isRunning());
                    } else {
                        Log.e(CHECK_SERVER_LOG_TAG, "Got Null body : " + response.code());
                        displayServerCheckResult(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CheckServer> call, @NonNull Throwable t) {
                    displayServerCheckResult(false);
                    Log.e(CHECK_SERVER_LOG_TAG, "Got Failure : " + t.getMessage(), t);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (errorText.getAlpha() > 0) {
            errorText.animate().alpha(0).setDuration(500);
        }
        displayButtonToProgress(new Runnable() {
            @Override
            public void run() {
                final String serverUrlString = serverUrl.getText().toString();
                final String usernameString  = username.getText().toString();
                EditText password = findViewById(R.id.edit_password);
                final String passwordString = password.getText().toString();
                if( serverUrlString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty() ) {
                    onFailed("Need to fill the 3 fields");
                    return;
                }
                Network network = new ApiModule("https://" + serverUrlString, LoginActivity.this)
                        .provideApi();
                JSONObject json = new JSONObject();
                try {
                    json.put("username", usernameString);
                    json.put("password", passwordString);
                } catch (JSONException e) {
                    Log.e(LOGIN_LOG_TAG, "JSON Exception : " + e.getMessage(), e);
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
                network.login(body).enqueue(LoginActivity.this);
            }
        });
    }

    @Override
    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
        if (response.body() != null) {
            Login login = response.body();
            if (login.isAuth()) {
                db.addConnection(new Connection(serverUrl.getText().toString(), username.getText().toString(), login.getRefreshToken(), login.getSessionToken()));
                onSuccess();
            } else {
                onFailed(response.body().getMessage());
                Log.e(LOGIN_LOG_TAG, "Got auth false : " + response.code() + " : " + response.body().getMessage());
            }
        } else if (response.errorBody() != null) {
            try {
                JSONObject json = new JSONObject(response.errorBody().string());
                onFailed(json.optString("message", ""));
            } catch (IOException | JSONException e) {
                onFailed("");
                Log.e(LOGIN_LOG_TAG, "Cannot parse the error body : " + response.code() + " : " + e.getMessage(), e);
            }
        } else {
            onFailed("");
            Log.e(LOGIN_LOG_TAG, "Got Null body : " + response.code());
        }
    }

    @Override
    public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
        onFailed("");
        Log.e(LOGIN_LOG_TAG, "Got Failure : " + t.getMessage(), t);
    }

    private void onSuccess() {
        animateProgressToSuccess(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        });
    }

    private void onFailed(String errorMessage) {
        animateProgressToError();
        if (!errorMessage.isEmpty()) {
            errorText.setText(errorMessage);
            errorText.animate().alpha(1).setDuration(500);
        }
    }
}


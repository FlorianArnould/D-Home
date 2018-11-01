package fr.socket.florian.dhome.view.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.database.Connection
import fr.socket.florian.dhome.database.Database
import fr.socket.florian.dhome.network.ApiCallback
import fr.socket.florian.dhome.network.ApiManager
import fr.socket.florian.dhome.network.model.CheckServer
import fr.socket.florian.dhome.network.model.Login
import fr.socket.florian.dhome.view.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_content.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = getColor(R.color.colorPrimary)

        rootLayout.setKeyboardStateListener(object : ContainerViewHandler.OnKeyboardStateChange {
            override fun onKeyboardShow() {
                TransitionManager.beginDelayedTransition(rootLayout)
            }

            override fun onKeyboardHide() {
                TransitionManager.beginDelayedTransition(rootLayout)
            }
        })
        val isNotLaunchActivity = intent.getBooleanExtra(IS_NOT_LAUNCH_ACTIVITY, false)
        if(isNotLaunchActivity) {
            val set = ConstraintSet()
            set.clone(this@LoginActivity, R.layout.activity_login_transition)
            set.applyTo(rootLayout)
            loginForm.visibility = View.VISIBLE
            loginForm.alpha = 1f
            back.visibility = View.VISIBLE
        } else {
            db = Database(this)
            Handler().postDelayed({
                db.getConnections { connections ->
                    if (connections.isEmpty()) {
                        TransitionManager.beginDelayedTransition(rootLayout)
                        val set = ConstraintSet()
                        set.clone(this@LoginActivity, R.layout.activity_login_transition)
                        set.applyTo(rootLayout)
                        loginForm.visibility = View.VISIBLE
                        loginForm.animate().alpha(1f).duration = 500
                    } else {
                        startMainActivity()
                    }
                }
            }, 1000)
        }
        loginForm.onServerUrlChanged = {url, callback ->
            ApiManager.checkServer(this@LoginActivity, url, object : ApiCallback<CheckServer>() {
                override fun onSuccess(response: CheckServer) {
                    callback(response.isRunning)
                }

                override fun onFailure(response: JSONObject?, code: Int) {
                    Log.e(CHECK_SERVER_LOG_TAG, code.toString())
                    callback(false)
                }

            })
        }

        loginForm.onSubmitClickListener = { url, username, password, callback ->
            ApiManager.login(this@LoginActivity, url, username, password, object : ApiCallback<Login>() {
                override fun onSuccess(response: Login) {
                    db.addConnection(Connection(editServerUrl.text.toString(), editUsername.text.toString(), response.refreshToken!!, response.sessionToken!!))
                    callback(true, "")
                }

                override fun onFailure(response: JSONObject?, code: Int) {
                    Log.e(LOGIN_LOG_TAG, code.toString())
                    if (response != null) {
                        callback(false, response.optString("message", ""))
                    } else {
                        callback(false, "")
                    }
                }
            })
        }

        back.setOnClickListener { onBackPressed() }

        loginForm.onFormFinished = {
            if(isNotLaunchActivity) {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            } else {
                startMainActivity()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    public override fun onDestroy() {
        if (this::db.isInitialized) db.close()
        super.onDestroy()
    }

    private fun startMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    companion object {
        private const val CHECK_SERVER_LOG_TAG = "Network check server"
        private const val LOGIN_LOG_TAG = "Network login"
        const val IS_NOT_LAUNCH_ACTIVITY = "is not launch activity"
    }
}


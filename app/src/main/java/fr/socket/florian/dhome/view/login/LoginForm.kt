package fr.socket.florian.dhome.view.login

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import fr.socket.florian.dhome.R
import kotlinx.android.synthetic.main.activity_login_content.view.*

class LoginForm : ConstraintLayout, View.OnFocusChangeListener, View.OnClickListener, TextView.OnEditorActionListener {
    var onSubmitClickListener: (url: String, username: String, password: String, callback: (succeed: Boolean, message: String) -> Unit) -> Unit = {_, _, _, _ -> }
    var onServerUrlChanged: (url: String, callback: (succeed: Boolean) -> Unit) -> Unit = { _, _ -> }
    var onFormFinished: () -> Unit = {}


    private fun initView() {
        inflate(context, R.layout.activity_login_content, this)
        editServerUrl.onFocusChangeListener = this
        button.setOnClickListener(this)
        editPassword.setOnEditorActionListener(this)
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        val serverUrlString = editServerUrl.text.toString()
        if (!hasFocus) {
            if (serverUrlString.isEmpty()) {
                displayServerCheckResult(false)
                return
            }
            displayServerCheckProgressBar()
            onServerUrlChanged(serverUrlString) { succeed -> displayServerCheckResult(succeed) }
        }
    }

    private fun displayServerCheckProgressBar() {
        if (serverCheckImage.alpha > 0) {
            serverCheckImage.animate().alpha(0f).duration = 500
        }
        serverCheckProgress.animate().alpha(1f).duration = 500
    }

    private fun displayServerCheckResult(succeed: Boolean) {
        serverCheckProgress.animate().alpha(0f).duration = 500
        Handler().postDelayed({
            if (succeed) {
                serverCheckImage.setColorFilter(getColor(context, R.color.green))
                serverCheckImage.setImageResource(R.drawable.ic_check)
            } else {
                serverCheckImage.setColorFilter(getColor(context, R.color.red))
                serverCheckImage.setImageResource(R.drawable.ic_close)
            }
            serverCheckImage.animate().alpha(1f).duration = 500
        }, 500)
    }

    override fun onClick(view: View?) {
        if (errorText.alpha > 0) {
            errorText.animate().alpha(0f).duration = 500
        }
        displayButtonToProgress(Runnable {
            val serverUrlString = editServerUrl.text.toString()
            val usernameString = editUsername.text.toString()
            val passwordString = editPassword.text.toString()
            if (serverUrlString.isEmpty() || usernameString.isEmpty() || passwordString.isEmpty()) {
                onFailed("Need to fill the 3 fields")
                return@Runnable
            }
            onSubmitClickListener(serverUrlString, usernameString, passwordString) { succeed, message -> if(succeed) onSuccess() else onFailed(message) }
        })
    }

    private fun displayButtonToProgress(runnable: Runnable) {
        button.isEnabled = false
        buttonText.animate().alpha(0f).duration = 500
        Handler().postDelayed({
            buttonProgress.animate().alpha(1f).duration = 500
            runnable.run()
        }, 500)
    }

    private fun animateProgressToSuccess(runnable: Runnable) {
        buttonProgress.animate().alpha(0f).duration = 500
        Handler().postDelayed({
            val transitionDrawable = TransitionDrawable(arrayOf(getDrawable(context, R.drawable.login_button_normal), getDrawable(context, R.drawable.login_button_success)))
            button.background = transitionDrawable
            transitionDrawable.isCrossFadeEnabled = true
            transitionDrawable.startTransition(500)
            resultButtonIcon.setImageResource(R.drawable.ic_check)
            resultButtonIcon.animate().alpha(1f).duration = 500
            Handler().postDelayed({ runnable.run() }, 500)
        }, 500)
    }

    private fun animateProgressToError() {
        buttonProgress.animate().alpha(0f).duration = 500
        Handler().postDelayed({
            val transitionDrawable = TransitionDrawable(arrayOf(getDrawable(context, R.drawable.login_button_normal), getDrawable(context, R.drawable.login_button_error)))
            button.background = transitionDrawable
            transitionDrawable.isCrossFadeEnabled = true
            transitionDrawable.startTransition(500)
            resultButtonIcon.setImageResource(R.drawable.ic_close)
            resultButtonIcon.animate().alpha(1f).duration = 500
            Handler().postDelayed({
                resultButtonIcon.animate().alpha(0f).duration = 500
                transitionDrawable.reverseTransition(500)
                Handler().postDelayed({
                    buttonText.animate().alpha(1f).duration = 500
                    button.isEnabled = true
                }, 500)
            }, 1000)
        }, 500)
    }

    private fun onSuccess() {
        animateProgressToSuccess(Runnable { onFormFinished() })
    }

    private fun onFailed(errorMessage: String) {
        animateProgressToError()
        if (!errorMessage.isEmpty()) {
            errorText.text = errorMessage
            errorText.animate().alpha(1f).duration = 500
        }
    }

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
            button.performClick()
        }
        return false
    }
}
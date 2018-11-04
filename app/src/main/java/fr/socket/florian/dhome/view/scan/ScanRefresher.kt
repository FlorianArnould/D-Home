package fr.socket.florian.dhome.view.scan

import android.os.CountDownTimer
import fr.socket.florian.dhome.network.ApiManager

class ScanRefresher(private val apiManager: ApiManager, private val update: (String) -> Unit) {

    private var countDownTimer: CountDownTimer? = null
    private lateinit var id: String
    private var waitingToStart: Boolean = false
    var isFinished: Boolean = false

    fun create() {
        apiManager.createScanner{stream ->
            if(stream != null) {
                id = stream.id
                if (waitingToStart) {
                    start()
                    waitingToStart = false
                }
            }
        }
    }

    fun start() {
        if (!this::id.isInitialized) {
            waitingToStart = true
            return
        }
        countDownTimer = object: CountDownTimer(Long.MAX_VALUE, 5000) {
            override fun onTick(p0: Long) {
                apiManager.readScanner(id) { content ->
                    if(content != null) {
                        isFinished = content.isFinished
                        if (content.isFinished) countDownTimer?.cancel()
                        update(content.data)
                    }
                }
            }

            override fun onFinish() {
                if (!isFinished) start()
            }
        }
        countDownTimer?.start()
    }

    fun pause() {
        waitingToStart = false
        countDownTimer?.cancel()
    }

    fun finish() {
        countDownTimer?.cancel()
        if (this::id.isInitialized) apiManager.deleteScanner(id) {}
    }
}
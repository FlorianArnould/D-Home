package fr.socket.florian.dhome.view.scan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.network.ApiManager
import fr.socket.florian.dhome.view.MainFragment
import java.lang.StringBuilder

class ScanFragment : MainFragment() {
    private val apiManager: ApiManager = ApiManager()
    private lateinit var scanRefresher: ScanRefresher

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.text_view, container, false)
        val statusImageView = view.findViewById<ImageView>(R.id.status)
        val textView = view.findViewById<TextView>(R.id.text)

        scanRefresher = ScanRefresher(apiManager) { data ->
            textView.text = StringBuilder(textView.text.toString()).append(data).toString()
        }

        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        scaleDown.duration = 1000
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        scaleUp.duration = 1000

        scaleDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                statusImageView.startAnimation(scaleUp)
            }
        })

        scaleUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                if (scanRefresher.isFinished) setImageFinished(statusImageView)
                else statusImageView.startAnimation(scaleDown)
            }
        })

        statusImageView.startAnimation(scaleUp)
        return view
    }

    private fun setImageFinished(imageView: ImageView) {
        imageView.setColorFilter(getColor(context!!, R.color.green))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setTitle(R.string.scanner)
        apiManager.initialize(context, 1){
            scanRefresher.create()
        }
    }

    override fun onResume() {
        super.onResume()
        scanRefresher.start()
    }

    override fun onPause() {
        super.onPause()
        scanRefresher.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        scanRefresher.finish()
        apiManager.close()
    }
}
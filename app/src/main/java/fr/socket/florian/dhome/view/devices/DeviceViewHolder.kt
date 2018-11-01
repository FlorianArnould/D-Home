package fr.socket.florian.dhome.view.devices

import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.util.Random

import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.network.model.Device

internal class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private val name: TextView = view.findViewById(R.id.name)
    private val description: TextView = view.findViewById(R.id.description)
    private val layout: RelativeLayout = view.findViewById(R.id.layout)
    private val transitionDrawable: TransitionDrawable = view.context.getDrawable(R.drawable.transition_green_red) as TransitionDrawable
    private val progress: ProgressBar = view.findViewById(R.id.progress)
    private var state: Boolean = false

    init {
        transitionDrawable.isCrossFadeEnabled = true
        view.setOnClickListener(this)
    }

    fun update(device: Device) {
        name.text = device.name
        description.text = device.description
        if (Random().nextBoolean()) {
            layout.background = transitionDrawable
            state = true
        } else {
            transitionDrawable.startTransition(0)
            layout.background = transitionDrawable
            state = false
        }
    }

    override fun onClick(view: View) {
        if (state) {
            progress.indeterminateDrawable.setTint(view.context.getColor(R.color.red))
        } else {
            progress.indeterminateDrawable.setTint(view.context.getColor(R.color.green))
        }
        progress.animate().alpha(1f).duration = 500
        Handler().postDelayed({
            if (state) {
                Handler().postDelayed({
                    progress.animate().alpha(0f).duration = 500
                    transitionDrawable.startTransition(500)
                }, 500)
            } else {
                Handler().postDelayed({
                    progress.animate().alpha(0f).duration = 500
                    transitionDrawable.reverseTransition(500)
                }, 500)
            }
            state = !state
        }, 1000)
    }
}

package fr.socket.florian.dhome.view.login

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class ContainerViewHandler : ConstraintLayout {

    private var isKeyboardShown: Boolean = false
    private var listener: OnKeyboardStateChange? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setKeyboardStateListener(listener: OnKeyboardStateChange) {
        this.listener = listener
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && isKeyboardShown) {
            // Keyboard is hiding
            isKeyboardShown = false
            listener!!.onKeyboardHide()
        }
        return super.dispatchKeyEventPreIme(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val proposedHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val actualHeight = height
        if (actualHeight > proposedHeight && !isKeyboardShown) {
            isKeyboardShown = true
            listener!!.onKeyboardShow()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Callbacks
    interface OnKeyboardStateChange {
        fun onKeyboardShow()

        fun onKeyboardHide()
    }
}

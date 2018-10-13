package fr.socket.florian.dhome.view.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ContainerViewHandler extends ConstraintLayout {

    private boolean isKeyboardShown;
    private OnKeyboardStateChange listener;

    public ContainerViewHandler(Context context) {
        super(context);
    }

    public ContainerViewHandler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerViewHandler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setKeyboardStateListener(OnKeyboardStateChange listener) {
        this.listener = listener;
    }

    @Override
    public boolean dispatchKeyEventPreIme(@NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isKeyboardShown) {
            // Keyboard is hiding
            isKeyboardShown = false;
            listener.onKeyboardHide();
        }
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        if (actualHeight > proposedHeight && !isKeyboardShown) {
            isKeyboardShown = true;
            listener.onKeyboardShow();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // Callbacks
    public interface OnKeyboardStateChange {
        void onKeyboardShow();

        void onKeyboardHide();
    }
}

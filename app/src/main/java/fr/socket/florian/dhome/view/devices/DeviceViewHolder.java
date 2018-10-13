package fr.socket.florian.dhome.view.devices;

import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import fr.socket.florian.dhome.R;

class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView text;
    private final RelativeLayout layout;
    private final TransitionDrawable transitionDrawable;
    private final ProgressBar progress;
    private boolean state;

    DeviceViewHolder(@NonNull View view) {
        super(view);
        text = view.findViewById(R.id.text);
        layout = view.findViewById(R.id.layout);
        progress = view.findViewById(R.id.progress);
        transitionDrawable = (TransitionDrawable) view.getContext().getDrawable(R.drawable.transition_green_red);
        if (transitionDrawable != null) {
            transitionDrawable.setCrossFadeEnabled(true);
            view.setOnClickListener(this);
        }
    }

    void update(String str) {
        text.setText(str);
        if (new Random().nextBoolean()) {
            layout.setBackground(transitionDrawable);
            state = true;
        } else {
            transitionDrawable.startTransition(0);
            layout.setBackground(transitionDrawable);
            state = false;
        }
    }

    @Override
    public void onClick(View view) {
        if (state) {
            progress.getIndeterminateDrawable().setTint(view.getContext().getColor(R.color.red));
        } else {
            progress.getIndeterminateDrawable().setTint(view.getContext().getColor(R.color.green));
        }
        progress.animate().alpha(1).setDuration(500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (state) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.animate().alpha(0).setDuration(500);
                            transitionDrawable.startTransition(500);
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.animate().alpha(0).setDuration(500);
                            transitionDrawable.reverseTransition(500);
                        }
                    }, 500);
                }
                state = !state;
            }
        }, 1000);
    }
}

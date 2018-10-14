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
import fr.socket.florian.dhome.network.model.Device;

class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView name;
    private final TextView description;
    private final RelativeLayout layout;
    private final TransitionDrawable transitionDrawable;
    private final ProgressBar progress;
    private boolean state;

    DeviceViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        layout = view.findViewById(R.id.layout);
        progress = view.findViewById(R.id.progress);
        transitionDrawable = (TransitionDrawable) view.getContext().getDrawable(R.drawable.transition_green_red);
        if (transitionDrawable != null) {
            transitionDrawable.setCrossFadeEnabled(true);
            view.setOnClickListener(this);
        }
    }

    void update(Device device) {
        name.setText(device.getName());
        description.setText(device.getDescription());
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

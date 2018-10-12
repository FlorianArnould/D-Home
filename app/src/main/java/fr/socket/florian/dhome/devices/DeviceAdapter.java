package fr.socket.florian.dhome.devices;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.socket.florian.dhome.R;

class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private final List<String> data;

    DeviceAdapter() {
        data = new ArrayList<>();
        data.add("alpha");
        data.add("beta");
        data.add("charlie");
        data.add("tango");
        Collections.reverse(data);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_card, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i) {
        deviceViewHolder.update(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

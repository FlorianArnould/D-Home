package fr.socket.florian.dhome.view.devices;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.network.model.Device;

class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private List<Device> devices;

    DeviceAdapter() {
        devices = new ArrayList<>();
    }

    void update(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_card, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, int i) {
        deviceViewHolder.update(devices.get(i));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }
}

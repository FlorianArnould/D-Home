package fr.socket.florian.dhome.view.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.network.model.Device
import java.util.*

internal class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {

    private var devices: List<Device> = ArrayList()

    fun update(devices: List<Device>) {
        this.devices = devices
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): DeviceViewHolder {
        return DeviceViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.device_card, parent, false))
    }

    override fun onBindViewHolder(deviceViewHolder: DeviceViewHolder, i: Int) {
        deviceViewHolder.update(devices[i])
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}

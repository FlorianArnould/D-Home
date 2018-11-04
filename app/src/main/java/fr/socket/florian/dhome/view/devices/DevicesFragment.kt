package fr.socket.florian.dhome.view.devices

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.network.ApiManager
import fr.socket.florian.dhome.view.MainActivity
import fr.socket.florian.dhome.view.MainFragment
import fr.socket.florian.dhome.view.login.LoginActivity

class DevicesFragment : MainFragment() {

    private val adapter: DeviceAdapter = DeviceAdapter()
    private var apiManager: ApiManager? = null

    init {
        // This shouldn't be called from other classes if there are some arguments
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.recycler_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val layoutManager = object : GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false) {
            override fun isLayoutRTL(): Boolean {
                return true
            }
        }
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter

        return view
    }

    private fun update(context: Context) {
        apiManager?.close()
        apiManager = ApiManager().initialize(context, 1) { api ->
            api.getAllDevices { devices ->
                if (devices == null) {
                    val activity = (activity as MainActivity)
                    activity.createIndefiniteSnackbar(R.string.cannot_reach_any_server, R.string.add_a_server) {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.putExtra(LoginActivity.IS_NOT_LAUNCH_ACTIVITY, true)
                        startActivityForResult(intent, NEW_LOGIN)
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                } else adapter.update(devices)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        update(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NEW_LOGIN) {
            update(context!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(R.string.nav_devices)
    }

    override fun onDestroy() {
        super.onDestroy()
        apiManager?.close()
    }

    companion object {
        private const val NEW_LOGIN = 1
    }
}
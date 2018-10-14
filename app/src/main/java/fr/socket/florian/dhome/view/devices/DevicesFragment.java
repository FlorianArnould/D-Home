package fr.socket.florian.dhome.view.devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.socket.florian.dhome.R;
import fr.socket.florian.dhome.network.ApiManager;
import fr.socket.florian.dhome.network.model.Device;
import fr.socket.florian.dhome.utils.Listener;
import fr.socket.florian.dhome.view.MainFragment;

public class DevicesFragment extends MainFragment {

    private final DeviceAdapter adapter;
    private ApiManager apiManager;

    public DevicesFragment() {
        // This shouldn't be called from other classes if there are some arguments
        adapter = new DeviceAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false) {
            @Override
            protected boolean isLayoutRTL() {
                return true;
            }
        };
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (apiManager != null) {
            apiManager.close();
        }
        apiManager = new ApiManager();
        apiManager.initialize(context, 1, new Listener<ApiManager>() {
            @Override
            public void callback(ApiManager api) {
                api.getAllDevices(new Listener<List<Device>>() {
                    @Override
                    public void callback(List<Device> devices) {
                        adapter.update(devices);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(R.string.nav_devices);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (apiManager != null) {
            apiManager.close();
        }
    }
}

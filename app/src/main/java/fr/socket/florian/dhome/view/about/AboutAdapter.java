package fr.socket.florian.dhome.view.about;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.socket.florian.dhome.R;

class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ICON_VIEW_TYPE = 1;
    private static final int HEADER_VIEW_TYPE = 2;
    private static final int GITHUB_VIEW_TYPE = 3;

    private final List<Integer> headers;
    private final List<Icon> icons;
    private final List<Github> githubs;

    private int headerCounter;
    private int iconsCounter;
    private int githubsCounter;

    AboutAdapter() {
        icons = new ArrayList<>();
        icons.add(new Icon(R.drawable.ic_light_bulb, "Icon made by Freepik from www.flaticon.com ", "http://www.freepik.com/"));
        icons.add(new Icon(R.drawable.ic_server, "Icon made by Smashicons from www.flaticon.com ", "https://smashicons.com/"));
        icons.add(new Icon(R.drawable.ic_user, "Icon made by Smashicons from www.flaticon.com ", "https://smashicons.com/"));
        icons.add(new Icon(R.drawable.ic_locked, "Icon made by Smashicons from www.flaticon.com ", "https://smashicons.com/"));
        headers = new ArrayList<>();
        headers.add(R.string.repositories);
        headers.add(R.string.icons);
        githubs = new ArrayList<>();
        githubs.add(new Github("Restful-home", "Node js backend API to control the home devices through a raspberry pi", "https://github.com/FlorianArnould/restful-home"));
        githubs.add(new Github("D-Home", "An android application to use the restful-home backend API", "https://github.com/FlorianArnould/D-Home"));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == githubs.size() + 1) {
            return HEADER_VIEW_TYPE;
        } else if (position <= githubs.size()) {
            return GITHUB_VIEW_TYPE;
        }
        return ICON_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
                return new HeaderViewHolder(view);
            case ICON_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_row, parent, false);
                return new IconViewHolder(view);
            case GITHUB_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.github_row, parent, false);
                return new GithubViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_row, parent, false);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            headerCounter = 0;
            iconsCounter = 0;
            githubsCounter = 0;
        }
        switch (getItemViewType(position)) {
            case ICON_VIEW_TYPE:
                ((IconViewHolder) viewHolder).update(icons.get(iconsCounter++));
                break;
            case HEADER_VIEW_TYPE:
                ((HeaderViewHolder) viewHolder).update(headers.get(headerCounter++));
                break;
            case GITHUB_VIEW_TYPE:
                ((GithubViewHolder) viewHolder).update(githubs.get(githubsCounter++));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return icons.size() + headers.size() + githubs.size();
    }
}

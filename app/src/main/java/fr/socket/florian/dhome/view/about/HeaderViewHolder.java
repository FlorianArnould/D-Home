package fr.socket.florian.dhome.view.about;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.socket.florian.dhome.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;

    public HeaderViewHolder(@NonNull View view) {
        super(view);
        title = view.findViewById(R.id.title);
    }

    public void update(@StringRes int stringRes) {
        title.setText(stringRes);
    }
}

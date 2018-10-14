package fr.socket.florian.dhome.view.about;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.socket.florian.dhome.R;

class IconViewHolder extends RecyclerView.ViewHolder {

    private final View view;
    private final ImageView icon;
    private final TextView message;

    IconViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        icon = view.findViewById(R.id.icon);
        message = view.findViewById(R.id.message);
    }

    void update(final Icon iconObject) {
        icon.setImageResource(iconObject.getDrawableRes());
        message.setText(iconObject.getMessage());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(iconObject.getLink()));
                view.getContext().startActivity(intent);
            }
        });
    }
}

package fr.socket.florian.dhome.view.about;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.socket.florian.dhome.R;

class GithubViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    private final TextView title;
    private final TextView description;

    GithubViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
    }

    void update(final Github github) {
        title.setText(github.getTitle());
        description.setText(github.getDescription());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(github.getLink()));
                view.getContext().startActivity(intent);
            }
        });
    }
}

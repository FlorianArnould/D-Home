package fr.socket.florian.dhome.view;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MainFragment extends Fragment {
    protected void setTitle(@StringRes int title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }
}

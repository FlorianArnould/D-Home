package fr.socket.florian.dhome.view

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class MainFragment : Fragment() {
    protected fun setTitle(@StringRes title: Int) {
        (activity as AppCompatActivity?)?.supportActionBar?.setTitle(title)
    }
}
,
package fr.socket.florian.dhome.view.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import fr.socket.florian.dhome.R

internal class AboutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val headers: List<Int> = listOf(R.string.repositories, R.string.icons)
    private val icons: List<Icon> = listOf(
            Icon(R.drawable.ic_light_bulb, "Icon made by Freepik from www.flaticon.com ", "http://www.freepik.com/"),
            Icon(R.drawable.ic_server, SMASHICONS_TEXT, SMASHICONS_URL),
            Icon(R.drawable.ic_user, SMASHICONS_TEXT, SMASHICONS_URL),
            Icon(R.drawable.ic_locked, SMASHICONS_TEXT, SMASHICONS_URL))
    private val githubs: List<Repository> = listOf(
            Repository("Restful-home", "Node js backend API to control the home devices through a raspberry pi", "https://github.com/FlorianArnould/restful-home"),
            Repository("D-Home", "An android application to use the restful-home backend API", "https://github.com/FlorianArnould/D-Home"))

    private var headerCounter: Int = 0
    private var iconsCounter: Int = 0
    private var repositoriesCounter: Int = 0

    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position == githubs.size + 1) {
            return HEADER_VIEW_TYPE
        } else if (position <= githubs.size) {
            return GITHUB_VIEW_TYPE
        }
        return ICON_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            HEADER_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.header_row, parent, false)
                return HeaderViewHolder(view)
            }
            ICON_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.icon_row, parent, false)
                return IconViewHolder(view)
            }
            GITHUB_VIEW_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.repository_row, parent, false)
                return RepositoryViewHolder(view)
            }
        }
        view = LayoutInflater.from(parent.context).inflate(R.layout.icon_row, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            headerCounter = 0
            iconsCounter = 0
            repositoriesCounter = 0
        }
        when (getItemViewType(position)) {
            ICON_VIEW_TYPE -> (viewHolder as IconViewHolder).update(icons[iconsCounter++])
            HEADER_VIEW_TYPE -> (viewHolder as HeaderViewHolder).update(headers[headerCounter++])
            GITHUB_VIEW_TYPE -> (viewHolder as RepositoryViewHolder).update(githubs[repositoriesCounter++])
        }
    }

    override fun getItemCount(): Int {
        return icons.size + headers.size + githubs.size
    }

    companion object {
        private const val SMASHICONS_TEXT = "Icon made by Smashicons from www.flaticon.com "
        private const val SMASHICONS_URL = "https://smashicons.com/"
        private const val ICON_VIEW_TYPE = 1
        private const val HEADER_VIEW_TYPE = 2
        private const val GITHUB_VIEW_TYPE = 3
    }
}

package fr.socket.florian.dhome.view.connections

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.database.Database
import fr.socket.florian.dhome.view.MainActivity
import fr.socket.florian.dhome.view.MainFragment
import fr.socket.florian.dhome.view.login.LoginActivity

class ConnectionsFragment : MainFragment() {

    private val adapter: ConnectionAdapter = ConnectionAdapter()
    private lateinit var db: Database
    private lateinit var fab: View
    private var fabX: Int = 0
    private var fabY: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.connections_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        fab = view.findViewById<View>(R.id.fab)
        fab.setOnClickListener {
            val array = IntArray(2)
            fab.getLocationInWindow(array)
            fabX = array[0] + fab.width/2
            fabY = array[1] + fab.height/2
            val activity = activity as MainActivity

            val anim = AnimationUtils.loadAnimation(context, R.anim.scale_down)
            anim.duration = 300
            anim.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    fab.visibility = View.INVISIBLE
                }
            })

            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(LoginActivity.IS_NOT_LAUNCH_ACTIVITY, true)

            fab.startAnimation(anim)
            activity.animateReveal(fabX, fabY)

            startActivityForResult(intent, NEW_LOGIN)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!this::db.isInitialized) db = Database(context)
        db.getConnections { connections -> adapter.update(connections) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(R.string.nav_connections)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::db.isInitialized) db.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == NEW_LOGIN) {
            db.getConnections { connections -> adapter.update(connections) }
            Handler().postDelayed({
                (activity as MainActivity).animateHide(fabX, fabY)
                val anim = AnimationUtils.loadAnimation(context, R.anim.scale_up)
                anim.duration = 300
                anim.setAnimationListener(object: Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {}
                    override fun onAnimationStart(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        fab.visibility = View.VISIBLE
                    }
                })
                fab.startAnimation(anim)
            }, 400)
        }
    }

    companion object {
        private const val NEW_LOGIN = 1
    }
}

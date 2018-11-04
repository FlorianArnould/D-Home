package fr.socket.florian.dhome.view

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

import fr.socket.florian.dhome.R
import fr.socket.florian.dhome.view.about.AboutFragment
import fr.socket.florian.dhome.view.connections.ConnectionsFragment
import fr.socket.florian.dhome.view.devices.DevicesFragment
import fr.socket.florian.dhome.view.scan.ScanFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlin.math.pow
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        window.statusBarColor = getColor(R.color.colorPrimary)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        inflateFragment(DevicesFragment())
        navigationView.setCheckedItem(R.id.nav_devices)

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(0, getStatusBarHeight(), 0, 0)
        drawerLayout.layoutParams = params
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_scanner -> inflateFragment(ScanFragment())
            R.id.nav_devices -> inflateFragment(DevicesFragment())
            R.id.nav_connections -> inflateFragment(ConnectionsFragment())
            R.id.nav_about -> inflateFragment(AboutFragment())
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun inflateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit()
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun animateReveal(x: Int, y: Int) {
        Log.d("coordinates", x.toString() + " " + y.toString() + " " + toolbar.bottom.toString())
        val finalRadius = sqrt(animationReveal.height.toFloat().pow(2) + animationReveal.width.toFloat().pow(2))
        val animation = ViewAnimationUtils.createCircularReveal(animationReveal, x, y, 0f, finalRadius)
        animation.duration = 500
        animationReveal.visibility = View.VISIBLE
        animation.start()
        Log.d("animate", "start animation")
    }

    fun animateHide(x: Int, y: Int) {
        val finalRadius = sqrt(animationReveal.height.toFloat().pow(2) + animationReveal.width.toFloat().pow(2))
        val animation = ViewAnimationUtils.createCircularReveal(animationReveal, x, y, finalRadius, 0f)
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                animationReveal.visibility = View.INVISIBLE
            }
        })
        animation.start()
    }

    fun createIndefiniteSnackbar(@StringRes textRes: Int, @StringRes actionRes: Int, action: () -> Unit) {
        val snackbar = Snackbar.make(fragmentLayout, textRes, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(actionRes) { action() }
        snackbar.show()
    }
}

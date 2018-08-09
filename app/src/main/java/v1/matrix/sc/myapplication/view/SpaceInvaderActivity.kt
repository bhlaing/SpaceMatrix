package v1.matrix.sc.myapplication.view

import android.app.Activity
import android.R.attr.y
import android.R.attr.x
import android.graphics.Point
import v1.matrix.sc.myapplication.view.widget.SpaceInvadersView
import android.view.Display
import android.os.Bundle



class SpaceInvaderActivity : Activity() {
    lateinit var spaceInvadersView: SpaceInvadersView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get a Display object to access screen details
        val display = windowManager.defaultDisplay
        // Load the resolution into a Point object
        val size = Point()
        display.getSize(size)

        // Initialize gameView and set it as the view
        spaceInvadersView = SpaceInvadersView(this, size.x.toFloat(), size.y.toFloat())
        setContentView(spaceInvadersView)

    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        spaceInvadersView.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        spaceInvadersView.pause()
    }

}
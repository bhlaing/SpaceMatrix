package v1.matrix.sc.myapplication.view

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import v1.matrix.sc.myapplication.view.widget.GameView
import android.view.Display





class GameActivity : AppCompatActivity() {
    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val display = windowManager.defaultDisplay

        //Getting the screen resolution into point object
        val size = Point()
        display.getSize(size)
        gameView = GameView(this,size.x.toFloat(),size.y.toFloat())
        setContentView(gameView)
    }
    override fun onPause() {
        super.onPause()
        gameView?.pause()
    }

    //running the game when activity is resumed
    override fun onResume() {
        super.onResume()
        gameView?.resume()
    }
}

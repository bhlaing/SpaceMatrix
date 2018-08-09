package v1.matrix.sc.myapplication.model

import android.graphics.RectF
import android.R.attr.y
import android.R.attr.x
import android.R.attr.y
import android.R.attr.x







class Bullet{
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    lateinit var rect: RectF

    // Which way is it shooting
    val UP = 0
    val DOWN = 1

    // Going nowhere
    var heading = -1
    var speed = 350f

    val width = 1
    var height = 0.0f

    var isActive: Boolean = false

  constructor(screenY : Float){
      height = screenY / 20
      isActive = false

      rect = RectF()
  }

    fun setInactive() { isActive = false }

    fun getImpactPointY() : Float {
        if (heading == DOWN) {
            return y + height;
        } else {
            return y;
        }
    }

    fun shoot(startX: Float, startY: Float, direction: Int): Boolean {
        if (!isActive) {
            x = startX
            y = startY
            heading = direction
            isActive = true
            return true
        }

        // Bullet already active
        return false
    }

    fun update(fps: Long) {

        // Just move up or down
        if (heading === UP) {
            y = y - speed / fps
        } else {
            y = y + speed / fps
        }

        // Update rect
        rect.left = x
        rect.right = x + width
        rect.top = y
        rect.bottom = y + height

    }
}

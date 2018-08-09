package v1.matrix.sc.myapplication.model

import android.graphics.Bitmap
import android.graphics.RectF
import java.util.*
import android.graphics.BitmapFactory
import android.R.attr.y
import android.R.attr.x
import android.R.attr.x
import android.R.attr.y
import android.R.attr.y
import android.R.attr.x










class Invader{
    var rect: RectF? = null

    var generator = Random()

    // The player ship will be represented by a Bitmap
    var bitmap1: Bitmap? = null
    var bitmap2: Bitmap? = null

    // How long and high our invader will be
    var length: Float = 0.toFloat()
    var height: Float = 0.toFloat()

    // X is the far left of the rectangle which forms our invader
    var x: Float = 0.toFloat()

    // Y is the top coordinate
    var y: Float = 0.toFloat()

    // This will hold the pixels per second speedthat the invader will move
    private var shipSpeed: Float = 0.toFloat()

    val LEFT = 1
    val RIGHT = 2

    // Is the ship moving and in which direction
    private var shipMoving = RIGHT

    var isVisible: Boolean = false
    constructor(invader1 : Bitmap,invader2 : Bitmap , row : Int, col : Int, screenX : Float, screenY : Float){
        rect = RectF()
        length = screenX / 20.0f
        height = screenY / 20.0f

        isVisible = true

        val padding = screenX / 25

        x = col * (length + padding)
        y = row * (length + padding / 4)

        // Initialize the bitmap
        bitmap1 = invader1
        bitmap2 = invader2

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                length.toInt(),
                height.toInt(),
                false)

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                length.toInt(),
                height.toInt(),
                false)

        // How fast is the invader in pixels per second
        shipSpeed = 40.0f
    }

    fun update(fps: Long) {
        if (shipMoving === LEFT) {
            x = x - shipSpeed / fps
        }

        if (shipMoving === RIGHT) {
            x = x + shipSpeed / fps
        }

        // Update rect which is used to detect hits
        rect!!.top = y
        rect!!.bottom = y + height
        rect!!.left = x
        rect!!.right = x + length


    }

    fun dropDownAndReverse() {
        if (shipMoving === LEFT) {
            shipMoving = RIGHT
        } else {
            shipMoving = LEFT
        }

        y = y + height

        shipSpeed = shipSpeed * 1.18f
    }

    fun takeAim(playerShipX: Float, playerShipLength: Float): Boolean {

        var randomNumber = -1

        // If near the player
        if (playerShipX + playerShipLength > x && playerShipX + playerShipLength < x + length || playerShipX > x && playerShipX < x + length) {

            // A 1 in 500 chance to shoot
            randomNumber = generator.nextInt(150)
            if (randomNumber == 0) {
                return true
            }

        }

        // If firing randomly (not near the player) a 1 in 5000 chance
        randomNumber = generator.nextInt(2000)
        return randomNumber == 0
    }
}

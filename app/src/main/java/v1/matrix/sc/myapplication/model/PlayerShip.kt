package v1.matrix.sc.myapplication.model

import android.graphics.Bitmap
import android.graphics.RectF

class PlayerShip(
                 var rect: RectF? = null,
                 var shipBitmap: Bitmap? = null,
                 var length: Float=0.0f,
                 var height: Float=0.0f,
                 var x: Float = 0.0f,
                 var y: Float = 0.0f,
                 var shipSpeed: Float = 0.0f,
                 var STOPPED: Int  = 0,
                 var LEFT: Int = 1,
                 var RIGHT: Int = 2,
                 var shipMoving: Int = STOPPED){
    init {
        rect = RectF()
        shipSpeed = 350.0f
    }
    constructor(bitmap: Bitmap, screenX: Float, screenY: Float) : this() {
        length = screenX/10.0f
        height = screenY/10.0f

        shipBitmap = bitmap

        shipBitmap = Bitmap.createScaledBitmap(shipBitmap,length.toInt(),height.toInt(),false)

        x = screenX / 2.0f
        y = screenY - 20.0f

        shipMoving = 350
    }

    public fun update(fps: Long){
        if(shipMoving == LEFT){
            x -= shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            x += shipSpeed / fps;
        }

        // Update rect which is used to detect hits
        rect?.top = y;
        rect?.bottom = y + height;
        rect?.left = x
        rect?.right = x + length;
    }
}

package v1.matrix.sc.myapplication.model

import java.util.*


/**
 * Created by angelxu on 7/7/18.
 */
class BlingBling(var maxX:Float,
                 var maxY:Float,
                 var minX:Float=0.0f,
                 var minY:Float=0.0f,
                 var x :Float,
                 var y :Float,
                 var speed :Int = Random().nextInt(10),
                 var boosting :Boolean = false) {
    fun update(playerSpeed: Int) {
        //animating the star horizontally left side
        //by decreasing x coordinate with player speed
        x -= playerSpeed
        x -= speed
        //if the star reached the left edge of the screen
        if (x < 0) {
            //starting the star from right edge
            //this will give a infinite scrolling background effect
            x = maxX
            y = Random().nextInt(maxY.toInt()).toFloat()
            speed = Random().nextInt(15)
        }
    }

    fun getStarWidth(): Float {
       return 10.0f
//        return Random().nextFloat() * (maxX - minX) + minX
    }
}
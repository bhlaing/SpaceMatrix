package v1.matrix.sc.myapplication.model

import android.graphics.Bitmap

/**
 * Created by angelxu on 2/7/18.
 */
class Player(val characterImage : Bitmap,
             var maxY:Float,
             var minY:Float,
             var x :Float = 50.0f,
             var y :Float = 50.0f,
             var speed :Int = 0,
             var boosting :Boolean = false){
 fun update() {x +=10}


}
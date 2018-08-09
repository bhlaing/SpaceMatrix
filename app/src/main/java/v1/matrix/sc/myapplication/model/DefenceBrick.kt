package v1.matrix.sc.myapplication.model

import android.graphics.RectF

class DefenceBrick{
    lateinit var rect : RectF
    var isVisible : Boolean = false

    constructor(row : Int,col : Int, shelterNumber : Int, screenX : Float , screenY : Float){
        val width = screenX / 90.0f
        val height = screenY / 40.0f

        isVisible = true

        // Sometimes a bullet slips through this padding.
        // Set padding to zero if this annoys you
        val brickPadding = 1

        // The number of shelters
        val shelterPadding = screenX / 9
        val startHeight = screenY - screenY / 9 * 2

        rect = RectF(
                col * width + brickPadding +
                        shelterPadding * shelterNumber +
                        shelterPadding + shelterPadding * shelterNumber,

                row * height + brickPadding + startHeight+100,

                col * width + width - brickPadding +
                        shelterPadding * shelterNumber +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight)
    }

    fun setInvisible() {
        isVisible = false
    }
}
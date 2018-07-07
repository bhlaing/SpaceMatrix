package v1.matrix.sc.myapplication.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by angelxu on 30/6/18.
 */
class MatrixView : View{
    var random = Random()
    var _width = 0.0f
    var _height = 0.0f
    lateinit var  canvas : Canvas
    lateinit var canvasBgBmp : Bitmap
    val fontSize = 40.0f
    var columnSize = 0.0f
    val fallingChars = charArrayOf('+','@','&','~','[',']','{','#','=','€','^','/','?','-')
//    val fallingChars = charArrayOf('s','u','n','c','o','r','p','m','a','t','r','i','x')
    lateinit var textPositionByCol: IntArray
    var paintText : Paint = Paint()
    var paintBg : Paint = Paint()
    var paintBgBmp : Paint = Paint()
    var paintInitBg : Paint =  Paint()

    constructor(context : Context , attrs : AttributeSet) :super(context,attrs) {
        paintText.style = Paint.Style.FILL
        paintText.color = Color.YELLOW
        paintText.textSize = fontSize

        paintBg.color = Color.BLACK
        paintBg.alpha = 5
        paintBg.style = Paint.Style.FILL

        paintBgBmp.color = Color.BLACK

        paintInitBg.color = Color.BLACK
        paintInitBg.alpha = 255
        paintInitBg.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _width = w.toFloat()
        _height = h.toFloat()
        canvasBgBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = Canvas(canvasBgBmp)
        canvas.drawRect(0.0f, 0.0f, _width, _height, paintInitBg)
        columnSize = _width / fontSize

        textPositionByCol = IntArray(columnSize.toInt() + 1)

        for (x in 0 until columnSize.toInt()) {
            textPositionByCol[x] = random.nextInt(height / 2) + 1
        }
    }
    private fun drawT(){
        for (i in 0 until textPositionByCol.size) {
            canvas.drawText("" + fallingChars[random.nextInt(fallingChars.size)], i * fontSize, textPositionByCol[i] * fontSize, paintText)
            if (textPositionByCol[i] * fontSize > height && Math.random() > 0.975) {
                textPositionByCol[i] = 0
            }
            textPositionByCol[i]++
        }
    }
    fun drawCanvas(){
        canvas.drawRect(0.0f, 0.0f, _width.toFloat(), _height.toFloat(), paintBg);
        drawT()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(canvasBgBmp, 0.0f, 0.0f, paintBgBmp);
        drawCanvas()
        invalidate()
    }
}

package v1.matrix.sc.myapplication.view.widget

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import android.view.SurfaceView
import v1.matrix.sc.myapplication.R
import v1.matrix.sc.myapplication.model.Player
import android.view.MotionEvent
import v1.matrix.sc.myapplication.model.BlingBling
import java.util.*
import android.provider.SyncStateContract.Helpers.update




/**
 * Created by angelxu on 2/7/18.
 */
class GameView : SurfaceView ,Runnable{
    //boolean variable to track if the game is playing or not
    @Volatile
    var playing: Boolean = false

    //the game thread
    private var gameThread: Thread? = null

    private var player: Player? = null

    private var canvas: Canvas? = null

    private var surfaceHolder: SurfaceHolder? = null

    private var paint: Paint? = null

    private var blings = listOf<BlingBling>()
    //Class constructor

    constructor(context: Context, screenX: Float, screenY: Float): super(context) {

        //initializing player object
        //this time also passing screen size to player constructor
        var  playerAvater = BitmapFactory.decodeResource(context.resources, R.drawable.player)

        player = Player(characterImage = playerAvater,
                        x= 75.0f,
                        y= 30.0f,
                        maxY = screenY - playerAvater.height,          //calculating maxY
                        minY = 0.0f     //top edge's y point is 0 so min y will always be zero
                         )

        //initializing drawing objects
        surfaceHolder = holder
        paint = Paint()
        initBackground(screenX,screenY)
    }
    private fun initBackground(screenMaxX : Float,screenMaxY : Float){
        val generator = Random()

        val starNums = 100
        for (i in 0 until starNums) {
            val x = generator.nextInt(screenMaxX.toInt())
            val y = generator.nextInt(screenMaxY.toInt())
            val s = BlingBling(x = x.toFloat(),y = y.toFloat(),maxX = screenMaxX,maxY = screenMaxY)
            blings += s
        }
    }
        override fun run() {
        while (playing){
            invalidate()
            update()
            draw()
            control()
        }
    }
    private fun update(){
        player?.update()
        for (blingbling in blings) {
            blingbling.update(player!!.speed)
        }
    }
    private fun draw(){
        if (surfaceHolder?.getSurface()!!.isValid()) {
            //lock canv
            canvas = surfaceHolder?.lockCanvas()
            canvas!!.drawColor(Color.BLACK)
            paint!!.color = Color.WHITE
            //drawing a background color
//            canvas?.drawColor(Color.BLACK)
            //Draw  player

            for (b in blings) {
                paint!!.strokeWidth = b.getStarWidth()
                canvas!!.drawPoint(b.x, b.y, paint)
            }
            canvas?.drawBitmap(
                    player?.characterImage,
                    player!!.x,
                    player!!.y,
                    paint)

            //Unlock canvas
            surfaceHolder?.unlockCanvasAndPost(canvas)
        }
    }
    private fun control(){
        try{
            Thread.sleep(17)
//            gameThread.sl(17)
        }catch (exception : InterruptedException){

        }
    }
    fun pause(){
        playing = false
        try{
            gameThread?.join()
        }catch(e : InterruptedException){

        }
    }

    fun resume() {
        //when the game is resumed
        //starting the thread again
        playing = true
        gameThread = Thread(this)
        gameThread!!.start()
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                player?.boosting = false
            }
            MotionEvent.ACTION_DOWN -> {
                player?.boosting = true
            }
        }
        return true
    }
}
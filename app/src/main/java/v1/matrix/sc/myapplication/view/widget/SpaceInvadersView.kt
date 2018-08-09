package v1.matrix.sc.myapplication.view.widget

import android.content.Context
import android.view.SurfaceView
import android.content.res.AssetFileDescriptor
import android.graphics.*
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import v1.matrix.sc.myapplication.R
import v1.matrix.sc.myapplication.model.DefenceBrick
import v1.matrix.sc.myapplication.model.Invader
import v1.matrix.sc.myapplication.model.PlayerShip
import java.io.IOException
import java.lang.reflect.Array.getLength
import android.provider.SyncStateContract.Helpers.update
import android.provider.MediaStore.Images.Media.getBitmap
import v1.matrix.sc.myapplication.model.Bullet

class SpaceInvadersView : SurfaceView, Runnable {
    lateinit var gameThread: Thread
    lateinit var ourHolder: SurfaceHolder
    @Volatile
    var playing = false
    var paused = true

    lateinit var canvas: Canvas
    lateinit var paint: Paint

    var fps: Long = 0
    var timeThisFrame: Long? = null

    var screenX: Float = 0.0f
    var screenY: Float = 0.0f

    lateinit var playerShip: PlayerShip
    lateinit var bullet: Bullet

    var invadersBullets = arrayOfNulls<Bullet>(200)
    var nextBullet: Int = 0
    var maxInvaderBullets = 10

    var invaders = arrayOfNulls<Invader>(60)
    var numInvaders = 0
    var numBricks = 0
    var bricks = arrayOfNulls<DefenceBrick>(400)

    lateinit var soundPool: SoundPool

    var playerExplodeID = -1
    var invaderExplodeID = -1
    var shootID = -1
    var damageShelterID = -1
    var uhID = -1
    var ohID = -1

    var score: Int = 0

    // Lives
    val lives = 3

    // How menacing should the sound be?
    var menaceInterval: Long = 1000
    // Which menace sound should play next
    var uhOrOh: Boolean = false
    // When did we last play a menacing sound
    var lastMenaceTime = System.currentTimeMillis()

    constructor (context: Context, x: Float, y: Float) : super(context) {
        ourHolder = holder
        paint = Paint()

        screenX = x
        screenY = y

        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)

        try {
            // Create objects of the 2 required classes
            val assetManager = context.assets
            var descriptor: AssetFileDescriptor

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg")
            shootID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("invaderexplode.ogg")
            invaderExplodeID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("damageshelter.ogg")
            damageShelterID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("playerexplode.ogg")
            playerExplodeID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("damageshelter.ogg")
            damageShelterID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("uh.ogg")
            uhID = soundPool.load(descriptor, 0)

            descriptor = assetManager.openFd("oh.ogg")
            ohID = soundPool.load(descriptor, 0)

        } catch (e: IOException) {
            Log.d("ERRORRR", e.message)
        }



        prepareLevel()
    }

    private fun prepareLevel() {
        val bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership)

        // stretch the bitmap to a size appropriate for the screen resolution

        // Here we will initialize all the game objects

        // Make a new player space ship
        playerShip = PlayerShip(bitmap = bitmap, screenX = screenX, screenY = screenY)
        // Prepare the players bullet
        bullet = Bullet(screenY);

        // Initialize the invadersBullets array

        // Prepare the players bullet
        bullet = Bullet(screenY)

// Initialize the invadersBullets array
        for (i in 0 until invadersBullets.size) {
            invadersBullets[i] = Bullet(screenY)
        }
        if(bullet.isActive){
            bullet.update(fps);
        }

        for (i in 0 until invadersBullets.size) {
            if (invadersBullets[i]!!.isActive) {
                invadersBullets[i]!!.update(fps)
            }
        }
        // Build an army of invaders
        numInvaders = 0
        for (column in 0..5) {
            for (row in 0..4) {
                invaders[numInvaders] = Invader(
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.invader2),
                        row, column, screenX, screenY)
                numInvaders++
            }
        }
        menaceInterval = 1000
        // Build the shelters

        for (shelterNumber in 0..3) {
            for (column in 0..2) {
                for (row in 0..2) {
                    bricks[numBricks] = DefenceBrick(row, column, shelterNumber, screenX, screenY)
                    numBricks++
                }
            }
        }
    }

    override fun run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            var startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update()
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1.0f, 1.0f, 0, 0, 1.0f);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1.0f, 1.0f, 0, 0, 1.0f);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
            }

            // Draw the frame
            draw()

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame!! >= 1) {
                fps = 1000 / timeThisFrame!!;
            }

        }
    }

    fun update() {

        var bumped = false
        var lost = false

        // Move the player's ship
        playerShip.update(fps);
        // Update the invaders if visible
        for (i in 0 until numInvaders) {

            if (invaders[i]!!.isVisible) {
                // Move the next invader
                invaders[i]!!.update(fps)

                // Does he want to take a shot?
                if (invaders[i]!!.takeAim(playerShip.x,
                                playerShip.length)) {

                    // If so try and spawn a bullet
                    if (invadersBullets[nextBullet]!!.shoot(invaders[i]!!.x + invaders[i]!!.length / 2,
                                    invaders[i]!!.y, bullet.DOWN)) {

                        // Shot fired
                        // Prepare for the next shot
                        nextBullet++

                        // Loop back to the first one if we have reached the last
                        if (nextBullet === maxInvaderBullets) {
                            // This stops the firing of another bullet until one completes its journey
                            // Because if bullet 0 is still active shoot returns false.
                            nextBullet = 0
                        }
                    }
                }

                // If that move caused them to bump the screen change bumped to true
                if (invaders[i]!!.x > screenX - invaders[i]!!.length || invaders[i]!!.x < 0) {

                    bumped = true

                }
            }

        }
        // Update all the invaders bullets if active

        // Did an invader bump into the edge of the screen
        if (bumped) {

            // Move all the invaders down and change direction
            for (i in 0 until numInvaders) {
                invaders[i]!!.dropDownAndReverse()
                // Have the invaders landed
                if (invaders[i]!!.y > screenY - screenY / 10) {
                    lost = true
                }
            }

            // Increase the menace level
            // By making the sounds more frequent
            menaceInterval = menaceInterval - 80
        }
        if (lost) {
            prepareLevel();
        }

        // Update the players bullet

        // Has the player's bullet hit the top of the screen


        // Has an invaders bullet hit the bottom of the screen

        // Has the player's bullet hit an invader

        // Has an alien bullet hit a shelter brick

        // Has a player bullet hit a shelter brick

        // Has an invader bullet hit the player ship

    }

    fun draw() {
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 255, 255, 255)

            // Draw the player spaceship
            canvas.drawBitmap(playerShip.shipBitmap,
                    (width/2 - playerShip.shipBitmap!!.width/2).toFloat(),
                    (height/1.1 - playerShip.shipBitmap!!.height/4).toFloat(),paint);
//            canvas.drawBitmap(playerShip.shipBitmap, playerShip.x, screenY - 50, paint);
            // Draw the invaders
            for (i in 0 until numInvaders) {
                if (invaders[i]!!.isVisible) {
                    if (uhOrOh) {
                        canvas.drawBitmap(invaders[i]!!.bitmap1, invaders[i]!!.x, invaders[i]!!.y, paint)
                    } else {
                        canvas.drawBitmap(invaders[i]!!.bitmap2, invaders[i]!!.x, invaders[i]!!.y, paint)
                    }
                }
            }
            // Draw the bricks if visible

            // Draw the players bullet if active
            if(bullet.isActive){
                canvas.drawRect(bullet.rect, paint);
            }

            // Draw the invaders bullets
            for(i in 0 until numBricks) {
                if (bricks[i]!!.isVisible) {
                    canvas.drawRect(bricks[i]!!.rect, paint)
                }
            }
            //Update all the invader's bullets if active
            for (i in 0 until invadersBullets.size) {
                if (invadersBullets[i]!!.isActive) {
                    canvas.drawRect(invadersBullets[i]!!.rect, paint)
                }
            }
            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 249, 129, 0)
            paint.textSize = 40.0f
            canvas.drawText("Score: $score   Lives: $lives", 10.0f, 50.0f, paint)

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas)
        }
    }

    fun pause(){
        playing = false
      try {
          gameThread.join()
      }catch (e :InterruptedException){
          Log.e("Error :", "Joining thread")
      }
    }

    fun resume(){
        playing = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

        when (motionEvent.action and MotionEvent.ACTION_MASK) {

        // Player has touched the screen
            MotionEvent.ACTION_DOWN -> {

                paused = false

                if (motionEvent.y > screenY - screenY / 8) {
                    if (motionEvent.x > screenX / 2) {
                        playerShip.shipMoving = playerShip.RIGHT
                    } else {
                        playerShip.shipMoving = playerShip.LEFT
                    }

                }

                if (motionEvent.y < screenY - screenY / 8) {
                    // Shots fired
                    if (bullet.shoot(playerShip.x+ playerShip.length / 2, screenY, bullet.UP)) {
                        soundPool.play(shootID, 1.0f, 1.0f, 0, 0, 1.0f)
                    }
                }
            }

        // Player has removed finger from screen
            MotionEvent.ACTION_UP ->

                if (motionEvent.y > screenY - screenY / 10) {
                    playerShip.shipMoving = playerShip.STOPPED
                }
        }

        return true
    }
}
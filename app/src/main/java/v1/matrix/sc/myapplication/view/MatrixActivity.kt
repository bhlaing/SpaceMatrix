package v1.matrix.sc.myapplication.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_matrix.*
import v1.matrix.sc.myapplication.R

class MatrixActivity : AppCompatActivity() {
    var name = "Billy"
    val goodafternoonText = "Good Afternoon   "
    val dotdotdot = "...   "
    val enterMatrixText = "The Matrix has you"
    val followText = "Follow the white rabbit .. "
    val handler = Handler()
    val r1 = Runnable {
        makeText()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix)
        tempButton.setOnClickListener{
            var intent  = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(r1, 2000)
    }
    fun makeText() {
       var lastDelay = 0L
        for(index in goodafternoonText.indices){
            lastDelay = type(index,0L,goodafternoonText,greetingText)
        }
        for(index in dotdotdot.indices){
            lastDelay = type(index,lastDelay,dotdotdot,greetingText)
        }
        for(index in name.indices){
            lastDelay = type(index,lastDelay,name,greetingText)
        }
        val r2 = Runnable {
            for(index in enterMatrixText.indices){
                lastDelay = type(index,lastDelay,enterMatrixText,greetingTextMatrix)
            }
        }
        handler.postDelayed(r2, 2000)

        val r3 = Runnable {
            for(index in followText.indices){
                greetingText.text = ""
               greetingTextMatrix.text = ""
                lastDelay = type(index,lastDelay,followText,greetingText)
            }
        }
        handler.postDelayed(r3, 7100)
    }

    fun type(index : Int,delayC : Long ,stringToType : String,textView : TextView):Long {
        var delay: Long

        when(delayC){
            0L-> delay = index*100L
            else-> delay = delayC+100L
        }

        val handler = Handler()
        val r = Runnable {
            textView.append(stringToType[index].toString())
        }
        handler.postDelayed(r, delay)
        return delay;
    }
}

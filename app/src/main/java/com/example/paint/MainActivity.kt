package com.example.paint

import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView

    lateinit var myCanvas: MyCanvas
    lateinit var layout: LinearLayout
    lateinit var redBtn: Button
    lateinit var blueBtn: Button
    lateinit var blackBtn: Button
    lateinit var greenBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation = findViewById(R.id.Nav)
        myCanvas = findViewById(R.id.myCanvas)
        layout = findViewById(R.id.colors_layout)

        redBtn = findViewById(R.id.red_btn)
        blackBtn = findViewById(R.id.black_btn)
        blueBtn = findViewById(R.id.blue_btn)
        greenBtn = findViewById(R.id.green_btn)

        redBtn.setOnClickListener {
            myCanvas.mColor = Color.RED
            myCanvas.pathInit()
            layout.visibility = View.INVISIBLE
        }

        blackBtn.setOnClickListener {
            myCanvas.mColor = Color.BLACK
            myCanvas.pathInit()
            layout.visibility = View.INVISIBLE
        }

        blueBtn.setOnClickListener {
            myCanvas.mColor = Color.BLUE
            myCanvas.pathInit()
            layout.visibility = View.INVISIBLE
        }

        greenBtn.setOnClickListener {
            myCanvas.mColor = Color.GREEN
            myCanvas.pathInit()
            layout.visibility = View.INVISIBLE
        }
        navigation.setOnItemSelectedListener {
           when(it.itemId){
               R.id.path -> {myCanvas.setBooleans(path = true)
                   myCanvas.mColor = Color.BLACK
                   myCanvas.pathInit()
                   layout.visibility = View.INVISIBLE
               true}
               R.id.circle -> {myCanvas.setBooleans(circle = true)
                   layout.visibility = View.INVISIBLE
               true}
               R.id.arrow -> {myCanvas.setBooleans(arrow = true)
                   layout.visibility = View.INVISIBLE
               true}
               R.id.rect -> {myCanvas.setBooleans(rect = true)
                   layout.visibility = View.INVISIBLE
               true}
               R.id.palette -> {
                   layout.visibility = View.VISIBLE
                   true
               }
               else -> true
           }
        }

    }


}
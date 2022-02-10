package com.example.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyCanvas(val myContext: Context, val attrs: AttributeSet) : View(myContext, attrs) {

    lateinit var path: Path
    lateinit var mPaint: Paint
    var mColor: Int = Color.BLACK
    var startX: Float = 0f
    var startY: Float = 0f
    var endX: Float = 0f
    var endY: Float = 0f

    lateinit var mCanvas: Canvas
    var removeLast = false

    var isPath = false
    var isRect = false
    var isCircle = false
    var isArrow = false
    val map = hashMapOf<Path, Paint>()
    val list = mutableListOf<Float>()
    val linesMap = hashMapOf<FloatArray, Paint>()
    val rectsMap = hashMapOf<Rect, Paint>()
    val ovalsMap = hashMapOf<Rect, Paint>()
    var linesArray = floatArrayOf()
    lateinit var rect: Rect


    init {
        isPath = true
        pathInit()
    }

    fun pathInit() {
        mPaint = Paint()
        path = Path()
        mPaint.apply {
            isAntiAlias = true
            color = mColor
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 7f
            style = Paint.Style.STROKE

        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            mCanvas = it
        }

        Log.d("onDraw", map.size.toString())


        for ((key, value) in map.entries) {
            canvas?.drawPath(key, value)
        }

        for ((key, value) in linesMap.entries) {
            canvas?.drawLines(key, value)
        }
        for ((key, value) in rectsMap.entries) {
            canvas?.drawRect(key, value)
        }
        for ((key, value) in ovalsMap.entries) {

            canvas?.drawOval(RectF(key), value)
        }

        if (removeLast){
            when {
                isCircle -> {
                    ovalsMap.remove(rect)
                }
                isRect -> rectsMap.remove(rect)
                isArrow -> {
                    linesMap.remove(linesArray)
                    list.clear()
                }
            }

        }


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var xPos: Float = event!!.x
        var yPos: Float = event.y
        if (isPath) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(xPos, yPos)
                }
                MotionEvent.ACTION_MOVE -> path.lineTo(xPos, yPos)
                MotionEvent.ACTION_UP -> map.put(path, mPaint)

            }
        } else if (isArrow) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> drawArrow(event)
                MotionEvent.ACTION_MOVE -> drawArrow(event)
                MotionEvent.ACTION_UP -> drawArrow(event)
            }

        } else if (isRect){
            when (event.action) {
                MotionEvent.ACTION_DOWN -> drawRectngleOrOval(event)
                MotionEvent.ACTION_MOVE -> drawRectngleOrOval(event)
                MotionEvent.ACTION_UP -> drawRectngleOrOval(event)
            }
        } else if (isCircle){
            when (event.action) {
                MotionEvent.ACTION_DOWN -> drawRectngleOrOval(event, true)
                MotionEvent.ACTION_MOVE -> drawRectngleOrOval(event, true)
                MotionEvent.ACTION_UP -> drawRectngleOrOval(event, true)
            }
        }

        invalidate()
        return true
    }

    fun drawRectngleOrOval(event: MotionEvent, isOval: Boolean = false ) {
        var xPos: Float = event.x
        var yPos: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = xPos
                startY = yPos
            }
            MotionEvent.ACTION_MOVE -> {
                rect = Rect()
                if (startX > xPos) {
                    rect.right = startX.toInt()
                    rect.left = xPos.toInt()
                } else {
                    rect.right = xPos.toInt()
                    rect.left = startX.toInt()
                }
                if(startY > yPos){
                    rect.bottom = startY.toInt()
                    rect.top = yPos.toInt()
                } else{
                    rect.bottom = yPos.toInt()
                    rect.top = startY.toInt()
                }
                removeLast = true
                if (isOval)
                    ovalsMap.put(rect,mPaint)
                else
                rectsMap.put(rect,mPaint)
            }
            MotionEvent.ACTION_UP -> {
                removeLast = false
                rect = Rect()
                if (startX > xPos) {
                    rect.right = startX.toInt()
                    rect.left = xPos.toInt()
                } else {
                    rect.right = xPos.toInt()
                    rect.left = startX.toInt()
                }
                if(startY > yPos){
                    rect.bottom = startY.toInt()
                    rect.top = yPos.toInt()
                } else{
                    rect.bottom = yPos.toInt()
                    rect.top = startY.toInt()
                }
                if(isOval)
                    ovalsMap.put(rect,mPaint)
                else
                rectsMap.put(rect,mPaint)
            }
        }
    }


    fun drawArrow(event: MotionEvent) {
        var xPos: Float = event.x
        var yPos: Float = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("ActionDown", xPos.toString() + yPos.toString())
                startX = xPos
                startY = yPos

                Log.d("ActionDown", list.size.toString())
            }
            MotionEvent.ACTION_MOVE -> {
                setArrow(xPos,yPos)
                removeLast = true

            }
            MotionEvent.ACTION_UP -> {
                Log.d("ActionUp", xPos.toString() + yPos.toString())
                setArrow(xPos, yPos)
               removeLast = false

            }
        }
    }

    fun setArrow(xPos: Float, yPos: Float){
        val PI = Math.PI
        endX = xPos
        endY = yPos
        var degreesInRadians225=225*PI/180
        var degreesInRadians135=135*PI/180

        var dx=endX- startX
        var dy=endY- startY
        var angle=Math.atan2(dy.toDouble(),dx.toDouble())

        var x225=endX+50*Math.cos(angle+degreesInRadians225);
        var y225=endY+50*Math.sin(angle+degreesInRadians225);
        var x135=endX+50*Math.cos(angle+degreesInRadians135);
        var y135=endY+50*Math.sin(angle+degreesInRadians135);



        list.add(startX)
        list.add(startY)
        list.add(endX)
        list.add(endY)

        list.add(endX)
        list.add(endY)

        list.add(x225.toFloat())
        list.add(y225.toFloat())

        list.add(endX)
        list.add(endY)
        list.add(x135.toFloat())
        list.add(y135.toFloat())
        linesArray = list.toFloatArray()

        linesMap.put(linesArray, mPaint)
    }

    fun setBooleans(
        path: Boolean = false,
        rect: Boolean = false,
        circle: Boolean = false,
        arrow: Boolean = false
    ) {
        isPath = path
        isRect = rect
        isCircle = circle
        isArrow = arrow
    }
}
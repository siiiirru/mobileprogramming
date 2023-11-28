package com.example.graphic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyCustomView(this)) //커스텀뷰 적용
    }
}

private class MyCustomView (context: Context) : View(context) {
    var path = Path()
    var paint = Paint()
    init {
        paint.isAntiAlias=true
        paint.strokeWidth=10f
        paint.style = Paint.Style.STROKE
        paint.color=Color.BLUE
    }
    override fun onTouchEvent(event:MotionEvent?) : Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
            path.moveTo(event.x,event.y)
                return true
            }
            MotionEvent.ACTION_MOVE,MotionEvent.ACTION_UP->{
                path.lineTo(event.x,event.y)
                this.invalidate() //현재의 상태를 invalidate해서 그림을 다시 그리게함.
                return true
            }
        }
        return true
    }
    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        canvas.drawPath(path,paint)
    }

}
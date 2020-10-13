package hu.bme.aut.simpledrawer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.bme.aut.simpledrawer.model.Line
import hu.bme.aut.simpledrawer.model.Point

class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        const val DRAWINGSTYLE_LINE = 1
        const val DRAWINGSTYLE_POINT = 2
    }

    var currentDrawingStyle = DRAWINGSTYLE_LINE

    private lateinit var paint: Paint

    private var startPoint: Point? = null

    private var endPoint: Point? = null

    var lines: MutableList<Line>? = null

    var points: MutableList<Point>? = null

    init {
        initPaint()
        initLists()
    }

    private fun initPaint() {
        paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5F
    }

    private fun initLists() {
        lines = mutableListOf()
        points = mutableListOf()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        endPoint = Point(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startPoint = Point(event.x, event.y)
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                when (currentDrawingStyle) {
                    DRAWINGSTYLE_POINT -> addPointToTheList(endPoint!!)
                    DRAWINGSTYLE_LINE -> addLineToTheList(startPoint!!, endPoint!!)
                }
                startPoint = null
                endPoint = null
            }
            else -> return false
        }
        invalidate()
        return true
    }

    private fun addPointToTheList(startPoint: Point) {
        points?.add(startPoint)
    }

    private fun addLineToTheList(startPoint: Point, endPoint: Point) {
        lines?.add(Line(startPoint, endPoint))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (point in points!!) {
            drawPoint(canvas, point)
        }
        for (line in lines!!) {
            drawLine(canvas, line.start, line.end)
        }
        when (currentDrawingStyle) {
            DRAWINGSTYLE_POINT -> drawPoint(canvas, endPoint)
            DRAWINGSTYLE_LINE -> drawLine(canvas, startPoint, endPoint)
        }
    }

    private fun drawPoint(canvas: Canvas, point: Point?) {
        if (point == null) {
            return
        }
        canvas.drawPoint(point.x, point.y, paint)
    }

    private fun drawLine(canvas: Canvas, startPoint: Point?, endPoint: Point?) {
        if (startPoint == null || endPoint == null) {
            return
        }
        canvas.drawLine(
            startPoint.x,
            startPoint.y,
            endPoint.x,
            endPoint.y,
            paint
        )
    }

    fun restoreObjects(points: MutableList<Point>?, lines: MutableList<Line>?) {
        this.points = points
        this.lines = lines
        invalidate()
    }

    fun clear(){
        lines?.clear()
        points?.clear()
        invalidate()
    }
}
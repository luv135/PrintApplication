package com.unistrong.luowei.printer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by luowei on 2017/11/2.
 */
class Printer : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val midHeight = 152
    private val bottomHeight = 88
    private val tickWidth = 158 //纸宽度
    private val barWidth = 30 //
    private val barheight = 17 //
    private val leftWidth = 70
    private val whitHeight = 225    //打印机图顶部到进纸位置高度
    private val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.printer_1)
    private val mask: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.printer_mask)
    private val tick: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tick).let {
        val rate = it.width.toFloat() / tickWidth
        Bitmap.createScaledBitmap(it, tickWidth, (it.height / rate).toInt(), true)
    }
    private val topHeight = 78  //打印机顶部出纸高度

    private val tickHeight = tick.height //需要打印的纸张高度

    private val startY = bitmap.height - (bitmap.height - topHeight) / 2 - topHeight

    private val bottomPrint = startY + bitmap.height

    private var offset = whitHeight
    private var barOffset = 0
    private var barMove = 1


    private val maskPaint = Paint()
    private val tickClearPaint = Paint()
    val tickCanvas: Canvas
    val output: Bitmap
    val whitRect = Rect(0, startY + whitHeight, tickWidth, startY + whitHeight + tickHeight)
    val whitPaint = Paint()
    val barPaint = Paint()

    init {

        output = Bitmap.createBitmap(tickWidth,
                bitmap.height.shl(1), Bitmap.Config.ARGB_8888)
        tickCanvas = Canvas(output)
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        tickClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        whitPaint.color = Color.WHITE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val initialWidth = bitmap.width
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(initialWidth, View.MeasureSpec.EXACTLY)
        val initialHeight = bitmap.height.shl(1)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(initialHeight, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.RED)
//        canvas.save()
//        canvas.translate(0f, startY.toFloat())

        canvas.drawBitmap(bitmap, 0f, startY.toFloat(), null)
//        canvas.restore()

//        tickCanvas.drawText("我尽快叠飞机的房间看", 0f, 0f, maskPaint)

//        tickCanvas.save()
//        tickCanvas.translate(0f, startY.toFloat())

        tickCanvas.drawPaint(tickClearPaint)

        tickCanvas.drawBitmap(tick, 0f, startY.toFloat() + offset.toFloat(), null)
        tickCanvas.drawBitmap(mask, 0f, startY.toFloat(), maskPaint)

        tickCanvas.drawRect(whitRect, whitPaint)


        tickCanvas.drawRect((barOffset).toFloat(), (startY + whitHeight).toFloat(),
                (barOffset + barWidth).toFloat(), (startY + whitHeight + barheight).toFloat(),
                barPaint)


//        tickCanvas.restore()

        if (offset > -tick.height + topHeight) {
            offset--
            if (whitRect.top < whitRect.bottom) {
                whitRect.bottom--
            }
            barOffset += barMove
            if (barOffset > tickWidth - barWidth) {
                barMove = -2
            } else if (barOffset < 0) {
                barMove = 2
            }
            postInvalidate()
        }
        canvas.drawBitmap(output, leftWidth.toFloat(), 0f, null)

//        canvas.restore()


        postInvalidate()

    }
}
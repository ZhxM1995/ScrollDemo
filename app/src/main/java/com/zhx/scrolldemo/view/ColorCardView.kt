package com.zhx.scrolldemo.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import android.widget.Scroller
import androidx.customview.widget.ViewDragHelper
import javax.security.auth.callback.Callback


/**
 * author: zhx
 * date: 2020/10/10
 * description:
 */
class ColorCardView : View {

    var lastX = 0f
    var lastY = 0f
    var scroller = Scroller(context)


    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        canvas?.drawRect(0f, 0f, mWidth.toFloat(), mWidth.toFloat(), mPaint)
//        Log.d("scrollview", "onDraw")
//    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            (parent as View).scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var rawX = event?.x ?: 0f
        var rawY = event?.y ?: 0f
        var officeX = 0
        var officeY = 0
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("scrollview", "down")
                lastX = rawX
                lastY = rawY
            }
            MotionEvent.ACTION_MOVE -> {
                officeX = rawX.toInt() - lastX.toInt()
                officeY = rawY.toInt() - lastY.toInt()
                Log.d("scrollview", "officeX ${officeX + left}")
                Log.d("scrollview", "officeY ${officeY + top}")
                (parent as View).scrollBy(-officeX,-officeY)
//                layout(
//                    left + officeX,
//                    top + officeY,
//                    right + officeX,
//                    bottom + officeY
//                )
//                var layoutParams = layoutParams as LinearLayout.LayoutParams
//                layoutParams.leftMargin = left + officeX
//                layoutParams.topMargin = top + officeY
//                setLayoutParams(layoutParams)
            }
            MotionEvent.ACTION_UP -> {
                val viewGroup = parent as View
                scroller.startScroll(
                    viewGroup.scrollX,
                    viewGroup.scrollY,
                    -viewGroup.scrollX,
                    -viewGroup.scrollY
                )
                invalidate()
            }
        }
        return true
    }



    private fun initPaint() {
        setBackgroundColor(Color.BLUE)
    }

}
package com.zhx.scrolldemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper


/**
 * author: miaozhenxing662@hellobike.com
 * date: 2020/10/12
 * description:
 */
class DragViewGroup : FrameLayout {

    private lateinit var mViewDragHelper: ViewDragHelper
    private lateinit var mMenuView: View
    private lateinit var mMainView: View
    private var mWidth = 0

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        mViewDragHelper = ViewDragHelper.create(this, callback)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.let { mViewDragHelper.shouldInterceptTouchEvent(it) }
            ?: super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //将触摸事件传递给ViewDragHelper，此操作必不可少
        event?.let { mViewDragHelper.processTouchEvent(it) }
        return true
    }

    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mMenuView = getChildAt(0)
        mMainView = getChildAt(1)
    }

    // 获取View的宽度、高度
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mWidth = mMenuView.measuredWidth
    }

    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        // tryCaptureView() 指定在创建ViewDragHelper时，参数parentView中的哪一个子
        // View可以被移动，本例中ViewGroup定义了两个子View--MenuView和MainView，其中
        // 只有MainView 是可以被拖动的
        // 何时开始检测触摸事件
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            //如果当前触摸的child是mMainView时开始检测
            return mMainView === child
        }

        // 处理垂直滑动，默认0 --- 不发生滑动
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return 0
        }

        // 处理水平滑动
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        // 拖动结束后调用，是指离开屏幕后实现的操作
        override fun onViewReleased(
            releasedChild: View,
            xvel: Float,
            yvel: Float
        ) {
            super.onViewReleased(releasedChild, xvel, yvel)
            //手指抬起后缓慢移动到指定位置
            if (mMainView.left < 300) {
                //关闭菜单,mMainView 移动后左边距小于300px，将MainView还原到初始状态，即（0,0）
                //ViewDragHelper中的smoothSlideViewTo相当于Scroller的startScroll方法
//                        Log.e("mMainView.getLeft()", "" + mMainView.getLeft());
                mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0)
                ViewCompat.postInvalidateOnAnimation(this@DragViewGroup)
                // mSroller.startScroll(x, y, dx ,dy);
                // invalidate();
            } else {
                //打开菜单，将MainView移动到坐标（150,0），即显示MenuView
                mViewDragHelper.smoothSlideViewTo(mMainView, 150, 0)
                ViewCompat.postInvalidateOnAnimation(this@DragViewGroup)
            }
        }
    }

}
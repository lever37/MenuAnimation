package com.practicum.menuanimation

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import kotlin.math.abs

class MenuButtonSwipeHandler(
    private val activity: MainActivity,
    private val swipedView: View
) : View.OnTouchListener, GestureDetector.OnGestureListener {

    private var gestureDetector: GestureDetector? = null

    init {
        gestureDetector = GestureDetector(swipedView.context, this)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector?.onTouchEvent(event!!) ?: false
    }

    override fun onDown(p0: MotionEvent): Boolean { return true }

    override fun onShowPress(p0: MotionEvent) {}

    override fun onSingleTapUp(p0: MotionEvent): Boolean { return false }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean { return false }

    override fun onLongPress(p0: MotionEvent) {}

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val diffX: Float = e2.x - e1.x
        val diffY: Float = e2.y - e1.y

        val menuContainer = activity.binding.menuContainer
        val currentIndex = menuContainer.indexOfChild(swipedView)

        if (abs(diffX) > abs(diffY) && abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

            if (diffX < 0) {
                // Свайп влево
                removeMenuButton(menuContainer, currentIndex)
            } else {
                // Свайп вправо
                removeMenuButton(menuContainer, currentIndex)
            }
        }

        return true
    }

    private fun removeMenuButton(menuContainer: LinearLayout, currentIndex: Int) {

        if (currentIndex >= 0 && currentIndex < menuContainer.childCount) {
            menuContainer.removeViewAt(currentIndex)
        }
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}
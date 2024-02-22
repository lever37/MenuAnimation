package com.practicum.menuanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.practicum.menuanimation.databinding.ActivityMainBinding


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var isMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuBut.setOnClickListener {
            if (!isMenuOpen) {
                addMenuButtons(binding.menuContainer)
            }
            toggleMenu(binding.menuContainer)
        }
    }

    private var isAnimationRunning = false

    private fun toggleMenu(menuContainer: LinearLayout) {
        if (isAnimationRunning) return // если анимация уже запущена, игнорируем повторные нажатия

        val slideAnimation = if (isMenuOpen) R.anim.slide_down else R.anim.slide_up
        val animation = AnimationUtils.loadAnimation(this, slideAnimation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                isAnimationRunning = true // устанавливаем флаг, что анимация запущена
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                isAnimationRunning = false // сбрасываем флаг, когда анимация завершается
                // Если меню закрыто, удаляем кнопки
                if (!isMenuOpen) {
                    menuContainer.removeAllViews()
                }
            }
        })
        menuContainer.startAnimation(animation)
        menuContainer.visibility = if (isMenuOpen) View.GONE else View.VISIBLE
        isMenuOpen = !isMenuOpen
    }

    fun removeMenuButton(index: Int) {
        val menuContainer = binding.menuContainer
        if (index >= 0 && index < menuContainer.childCount) {
            menuContainer.removeViewAt(index)
        }
    }

    private fun addMenuButtons(menuContainer: LinearLayout) {
        val menuIcons = arrayOf(
            Pair(R.drawable.settings, "Settings"),
            Pair(R.drawable.home, "Home"),
            Pair(R.drawable.search, "Search"),
            Pair(R.drawable.time, "Time"),
            Pair(R.drawable.squares, "Squares")
        )

        val buttonMargin = resources.getDimensionPixelSize(R.dimen.button_margin)

        menuIcons.forEachIndexed { index, item ->
            val (iconResId, labelText) = item
            val menuButton = Button(this)
            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.button_width),
                resources.getDimensionPixelSize(R.dimen.button_height)
            )
            params.setMargins(0, 0, 0, buttonMargin)
            menuButton.layoutParams = params
            menuButton.setBackgroundResource(R.drawable.button_style)
            menuButton.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0)
            menuButton.compoundDrawablePadding = -resources.getDimensionPixelSize(R.dimen.icon_padding)
            menuButton.text = labelText
            menuButton.setTextColor(ContextCompat.getColor(this, R.color.darkBlue))
            menuContainer.addView(menuButton)

            // Добавляем обработчик свайпа для каждой кнопки
            val menuSwipeHandler = MenuButtonSwipeHandler(this, menuButton)
            val gestureDetector = GestureDetector(this, menuSwipeHandler)
            menuButton.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
        }
    }

}
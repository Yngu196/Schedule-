package com.cherry.wakeupschedule

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cherry.wakeupschedule.service.SettingsManager

class ColorThemePickerActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager
    private lateinit var llThemesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_theme_picker)

        settingsManager = SettingsManager(this)
        llThemesContainer = findViewById(R.id.ll_themes_container)

        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }

        buildThemeCards()
    }

    private fun buildThemeCards() {
        val currentIndex = settingsManager.getCourseColorThemeIndex()

        settingsManager.colorThemes.forEachIndexed { index, theme ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = (16 * resources.displayMetrics.density).toInt()
                }
                setPadding(
                    (16 * resources.displayMetrics.density).toInt(),
                    (16 * resources.displayMetrics.density).toInt(),
                    (16 * resources.displayMetrics.density).toInt(),
                    (16 * resources.displayMetrics.density).toInt()
                )
                background = getCardBackground(index == currentIndex)
                isClickable = true
                isFocusable = true
                setOnClickListener { selectTheme(index) }
            }

            val title = TextView(this).apply {
                text = theme.name
                textSize = 16f
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = (12 * resources.displayMetrics.density).toInt()
                }
            }
            card.addView(title)

            val colorRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            theme.colors.take(4).forEach { color ->
                val colorDot = View(this).apply {
                    val size = (40 * resources.displayMetrics.density).toInt()
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        marginEnd = (8 * resources.displayMetrics.density).toInt()
                    }
                    background = getColorCircle(color)
                }
                colorRow.addView(colorDot)
            }
            card.addView(colorRow)

            val colorRow2 = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = (4 * resources.displayMetrics.density).toInt()
                }
            }

            theme.colors.drop(4).take(4).forEach { color ->
                val size = (40 * resources.displayMetrics.density).toInt()
                val colorDot = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        marginEnd = (8 * resources.displayMetrics.density).toInt()
                    }
                    background = getColorCircle(color)
                }
                colorRow2.addView(colorDot)
            }
            card.addView(colorRow2)

            val checkMark = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = (8 * resources.displayMetrics.density).toInt()
                }
                setImageResource(R.drawable.ic_check)
                setColorFilter(Color.WHITE)
                visibility = if (index == currentIndex) View.VISIBLE else View.GONE
                tag = index
            }
            card.addView(checkMark)

            llThemesContainer.addView(card)
        }
    }

    private fun getCardBackground(selected: Boolean): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12 * resources.displayMetrics.density
            setColor(Color.parseColor("#33FFFFFF"))
            if (selected) {
                setStroke(
                    (2 * resources.displayMetrics.density).toInt(),
                    Color.WHITE
                )
            }
        }
    }

    private fun getColorCircle(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
    }

    private fun selectTheme(index: Int) {
        settingsManager.setCourseColorThemeIndex(index)

        for (i in 0 until llThemesContainer.childCount) {
            val card = llThemesContainer.getChildAt(i) as? LinearLayout ?: continue
            val strokeDrawable = card.background as? GradientDrawable
            if (strokeDrawable != null) {
                val currentIndex2 = settingsManager.getCourseColorThemeIndex()
                if (i == currentIndex2) {
                    strokeDrawable.setStroke(
                        (2 * resources.displayMetrics.density).toInt(),
                        Color.WHITE
                    )
                } else {
                    strokeDrawable.setStroke(0, Color.TRANSPARENT)
                }
            }

            for (j in 0 until card.childCount) {
                val child = card.getChildAt(j)
                if (child is ImageView && child.tag == null) {
                    child.visibility = if (i == index) View.VISIBLE else View.GONE
                }
            }
        }

        Toast.makeText(this, "已选择: ${settingsManager.colorThemes[index].name}", Toast.LENGTH_SHORT).show()
    }
}

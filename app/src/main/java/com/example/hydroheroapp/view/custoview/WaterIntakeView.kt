package com.example.hydroheroapp.view.customview

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class WaterIntakeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentIntake = 0
    private var totalIntake = 3000
    private val maxSafeIntake = 4000

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("HydroHeroPrefs", Context.MODE_PRIVATE)
    private val handler = Handler(Looper.getMainLooper())
    private val dateCheckInterval: Long = 1000

    private val borderPaint = Paint().apply {
        color = if (isInNightMode()) Color.WHITE else Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val waterPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = if (isInNightMode()) Color.WHITE else Color.BLACK
        textSize = 48f
        textAlign = Paint.Align.CENTER
    }

    private var animatedIntake = 0f

    init {
        checkAndResetIntake()
        startDateCheckTask()
    }

    private fun isInNightMode(): Boolean {
        return (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = width.coerceAtMost(height) / 2 - borderPaint.strokeWidth

        val centerX = width / 2
        val centerY = height / 2

        canvas.drawCircle(centerX, centerY, radius, borderPaint)

        val waterLevel = animatedIntake / totalIntake.toFloat()
        val waterHeight = radius * 2 * waterLevel

        val path = Path().apply {
            addCircle(centerX, centerY, radius, Path.Direction.CW)
            close()
        }
        canvas.save()
        canvas.clipPath(path)

        canvas.drawRect(
            centerX - radius,
            centerY + radius - waterHeight,
            centerX + radius,
            centerY + radius,
            waterPaint
        )

        canvas.restore()

        val intakeText = "${animatedIntake.toInt()}/$totalIntake ml"
        canvas.drawText(intakeText, centerX, centerY + 20, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val width = width.toFloat()
            val height = height.toFloat()
            val centerX = width / 2
            val centerY = height / 2

            val radius = width.coerceAtMost(height) / 2 - borderPaint.strokeWidth
            val touchX = event.x
            val touchY = event.y

            val dx = touchX - centerX
            val dy = touchY - centerY
            if (dx * dx + dy * dy <= radius * radius) {
                showInputDialog()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun showInputDialog() {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Enter target amount (ml)"
        }
        AlertDialog.Builder(context).apply {
            setTitle("Set Target")
            setView(input)
            setPositiveButton("OK") { _, _ ->
                val target = input.text.toString().toIntOrNull()
                if (target != null && target <= maxSafeIntake) {
                    setTotalIntake(target)
                } else {
                    AlertDialog.Builder(context).apply {
                        setTitle("Invalid Input")
                        setMessage("Please enter a valid number below $maxSafeIntake ml.")
                        setPositiveButton("OK", null)
                        show()
                    }
                }
            }
            setNegativeButton("Cancel", null)
            show()
        }
    }

    fun addIntake(amount: Int) {
        if (currentIntake + amount > totalIntake) {
            currentIntake = totalIntake
        } else {
            currentIntake += amount
        }
        sharedPreferences.edit().putInt("currentIntake", currentIntake).apply()
        animateIntake()
    }

    private fun animateIntake() {
        val animator = ValueAnimator.ofFloat(animatedIntake, currentIntake.toFloat())
        animator.duration = 1000
        animator.addUpdateListener { animation ->
            animatedIntake = animation.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    fun setTotalIntake(total: Int) {
        totalIntake = total
        sharedPreferences.edit().putInt("totalIntake", totalIntake).apply()
        invalidate()
    }

    fun setIntake(intake: Int) {
        currentIntake = intake
        animatedIntake = intake.toFloat()
        invalidate()
    }

    private fun checkAndResetIntake() {
        val currentDate = getCurrentDate()
        val savedDate = sharedPreferences.getString("savedDate", null)

        if (savedDate == null || savedDate != currentDate) {
            currentIntake = 0
            sharedPreferences.edit().apply {
                putInt("currentIntake", currentIntake)
                putString("savedDate", currentDate)
                apply()
            }
            showResetDialog()
        } else {
            currentIntake = sharedPreferences.getInt("currentIntake", 0)
        }

        animatedIntake = currentIntake.toFloat()
        invalidate()
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun showResetDialog() {
        AlertDialog.Builder(context).apply {
            setTitle("Reminder")
            setMessage("Jangan lupa minum air hari ini ya !")
            setPositiveButton("OK", null)
            show()
        }
    }

    private fun startDateCheckTask() {
        handler.post(object : Runnable {
            override fun run() {
                checkAndResetIntake()
                handler.postDelayed(this, dateCheckInterval)
            }
        })
    }
}
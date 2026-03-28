package com.pradum.flashToast

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs

object FlashToast {

    const val SHORT = 2000L
    const val LONG = 3500L

    enum class ToastType { SUCCESS, ERROR, INFO, WARNING }

    private data class ToastData(
        val message: String,
        val type: ToastType,
        val duration: Long,
        val activityRef: WeakReference<Activity>
    )

    private val toastQueue: Queue<ToastData> = LinkedList()
    private var isShowing = false

    fun success(activity: Activity, message: String, duration: Long = SHORT) {
        showInternal(activity, message, ToastType.SUCCESS, duration)
    }

    fun error(activity: Activity, message: String, duration: Long = SHORT) {
        showInternal(activity, message, ToastType.ERROR, duration)
    }

    fun info(activity: Activity, message: String, duration: Long = SHORT) {
        showInternal(activity, message, ToastType.INFO, duration)
    }

    fun warning(activity: Activity, message: String, duration: Long = SHORT) {
        showInternal(activity, message, ToastType.WARNING, duration)
    }

    private fun showInternal(
        activity: Activity,
        message: String,
        type: ToastType,
        duration: Long = SHORT
    ) {
        toastQueue.add(ToastData(message, type, duration, WeakReference(activity)))
        if (!isShowing) showNext()
    }

    private fun showNext() {
        val next = toastQueue.poll() ?: run { isShowing = false; return }
        val activity = next.activityRef.get()
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            showNext() // skip dead activity
            return
        }
        isShowing = true
        displayNotification(activity, next)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayNotification(activity: Activity, data: ToastData) {
        val parent = activity.window.decorView as ViewGroup

        val layoutRes = getLayoutRes(data.type)
        val view = LayoutInflater.from(activity).inflate(layoutRes, parent, false)

        val txt = view.findViewById<TextView>(R.id.txtMessage)
        txt.text = data.message

        // --- PROGRESS BAR ---
        val progress = view.findViewById<ProgressBar>(R.id.progressBar)
        val animator = ValueAnimator.ofInt(100, 0).apply {
            duration = data.duration
            addUpdateListener { progress.progress = it.animatedValue as Int }
        }

        // --- END PROGRESS BAR ---
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.TOP }
        view.layoutParams = params
        parent.addView(view)

        // Offset below status bar
        view.post {
            val insets = ViewCompat.getRootWindowInsets(view)
            val statusBarHeight = insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
            val layoutParams = view.layoutParams as FrameLayout.LayoutParams
            layoutParams.topMargin = statusBarHeight
            view.layoutParams = layoutParams
        }

        // Entry animation
        view.translationY = -400f
        view.alpha = 0f
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .setInterpolator(OvershootInterpolator(1f))
            .start()

        val dismissRunnable = Runnable { dismiss(view, animator) }
        animator.start()
        view.postDelayed(dismissRunnable, data.duration)

        setupTouch(view, animator, dismissRunnable, data.duration)
    }

    private fun getLayoutRes(type: ToastType): Int {
        return when (type) {
            ToastType.SUCCESS -> R.layout.view_toast_success
            ToastType.ERROR -> R.layout.view_toast_error
            ToastType.INFO -> R.layout.view_toast_info
            ToastType.WARNING -> R.layout.view_toast_warning

            /*    ToastType.SUCCESS -> R.layout.view_custom_toast_success
                ToastType.ERROR -> R.layout.view_custom_toast_error
                ToastType.INFO -> R.layout.view_custom_toast_info
                ToastType.WARNING -> R.layout.view_custom_toast_warning*/
        }
    }

    private enum class SwipeDirection { LEFT, RIGHT }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouch(
        view: View,
        animator: ValueAnimator,
        dismissRunnable: Runnable,
        totalDuration: Long
    ) {
        var downX = 0f
        var isDragging = false
        val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animator.pause()
                    v.removeCallbacks(dismissRunnable)
                    downX = event.rawX
                    isDragging = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - downX
                    if (!isDragging && abs(deltaX) > touchSlop) isDragging = true
                    if (isDragging) {
                        v.translationX = deltaX
                        v.alpha = (1 - abs(deltaX) / 600f).coerceIn(0.3f, 1f)
                    }
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val deltaX = event.rawX - downX
                    val absX = abs(deltaX)
                    if (absX > 250) {
                        dismiss(
                            v,
                            animator,
                            if (deltaX > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT
                        )
                    } else {
                        v.animate().translationX(0f).alpha(1f).setDuration(200).start()
                        animator.resume()
                        val remaining = ((1f - animator.animatedFraction) * totalDuration).toLong()
                        v.postDelayed(dismissRunnable, remaining)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun dismiss(view: View, animator: ValueAnimator, direction: SwipeDirection? = null) {
        animator.cancel()
        if (view.parent == null) return

        val anim = view.animate().alpha(0f).setDuration(300)
        when (direction) {
            SwipeDirection.LEFT -> anim.translationX(-1000f)
            SwipeDirection.RIGHT -> anim.translationX(1000f)
            null -> anim.translationY(-400f)
        }

        anim.withEndAction {
            (view.parent as? ViewGroup)?.removeView(view)
            showNext()
        }.start()
    }
}
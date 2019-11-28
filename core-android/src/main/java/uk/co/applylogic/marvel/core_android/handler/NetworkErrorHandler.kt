package uk.co.applylogic.marvel.core_android.handler

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import android.view.MotionEvent
import java.util.*
import android.os.CountDownTimer
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.applylogic.marvel.core_android.R

const val MIN_TIME_BETWEEN_DISMISSALS = 1000L * 5 // 5 seconds
const val AUTO_DISMISSAL_DURATION = 2500L // 2.5 seconds
const val ANIMATION_DURATION = 250L // // 250 milliseconds
const val ALPHA_0 = 0F
const val ALPHA_1 = 1F
const val TAG = "NetworkErrorHandler"

interface NetworkErrorHandlerInterface {
    fun show(activity: FragmentActivity?, message: String?)
    fun dismiss(activity: FragmentActivity?)
}

class NetworkErrorHandler(val context: Context) : NetworkErrorHandlerInterface {

    lateinit var mainView: ViewGroup
    lateinit var errorView: View

    companion object {
        var lastDismissedTime: Long = 0
    }

    @Suppress("MagicNumber")
    override fun show(activity: FragmentActivity?, message: String?) {
        message?.let {
            Log.e(TAG, message)
        }
        if (Date().time - lastDismissedTime < MIN_TIME_BETWEEN_DISMISSALS)
            return
        GlobalScope.launch(Dispatchers.Main) {
            activity?.let {
                mainView = it.window.decorView.rootView as ViewGroup
                if (mainView.childCount > 0 && mainView.getChildAt(mainView.childCount - 1)?.findViewById<TextView>(
                        R.id.tvError
                    ) == null
                ) {
                    errorView = it.layoutInflater.inflate(R.layout.layout_error, mainView, false)
                    mainView.addView(errorView, -1)
                    val tvError = errorView.findViewById(R.id.tvError) as TextView
                    tvError.apply {
                        text = activity.getString(R.string.no_internet_available)
                        setOnTouchListener { _, event ->
                            when (event?.action) {
                                MotionEvent.ACTION_UP -> {
                                    dismiss(it)
                                }
                            }
                            true
                        }
                        fadeIn()
                    }

                    val timer =
                        object : CountDownTimer(AUTO_DISMISSAL_DURATION, AUTO_DISMISSAL_DURATION) {
                            @Suppress("EmptyFunctionBlock")
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                dismiss(it)
                            }
                        }
                    timer.start()
                }
            }
        }
    }

    override fun dismiss(activity: FragmentActivity?) {
        GlobalScope.launch(Dispatchers.Main) {
            activity?.let {
                mainView = it.window.decorView.rootView as ViewGroup
                if (mainView.childCount > 0) {
                    errorView = mainView.getChildAt(mainView.childCount - 1)
                    if (errorView.findViewById<TextView>(R.id.tvError) != null) {
                        val tvError = errorView.findViewById(R.id.tvError) as TextView?
                        tvError?.fadeOut()
                    }
                }
            }
        }
    }

    private fun View.fadeIn() {
        apply {
            alpha = ALPHA_0
            visibility = View.VISIBLE
            animate()
                .alpha(ALPHA_1)
                .duration = ANIMATION_DURATION
        }
    }

    private fun View.fadeOut() {
        apply {
            alpha = ALPHA_1
            animate()
                .setListener(object : Animator.AnimatorListener {
                    @Suppress("EmptyFunctionBlock")
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        mainView.removeView(errorView)
                        lastDismissedTime = Date().time
                    }

                    @Suppress("EmptyFunctionBlock")
                    override fun onAnimationCancel(animation: Animator?) {
                    }
                })
                .alpha(ALPHA_0)
                .duration = ANIMATION_DURATION
        }
    }
}


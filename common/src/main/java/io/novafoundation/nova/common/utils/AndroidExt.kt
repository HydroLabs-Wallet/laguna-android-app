package io.novafoundation.nova.common.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <T> Cursor.map(iteration: Cursor.() -> T): List<T> {
    val result = mutableListOf<T>()

    while (moveToNext()) {
        result.add(iteration())
    }

    return result
}

inline val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)


/**
 * use only with adjust resize
 * try to find view which has focus and request  keyboard hide for it
 */
fun View.hideKeyboardResize(listener: (() -> Unit)? = null) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val activity = context
    var view = rootView.findFocus()
    if (view == null) {
        view = View(activity)
    }
    val receiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(
            resultCode: Int,
            resultData: Bundle?
        ) {
            if (resultCode == InputMethodManager.RESULT_HIDDEN || resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN) {
                postDelayed({
                    listener?.invoke()
                }, 300)
            }
        }
    }
    var r = imm.hideSoftInputFromWindow(view.windowToken, 0, receiver)
}

fun View.hideKeyboardPan() {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val activity = context
    var view = rootView.findFocus()
    if (view == null) {
        view = View(activity)
    }
    var r = imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Explicitly call soft keyboard show as "request focus" is not enough
 * in a lot of cases (view will receive focus but keyboard might not show up)
 * @param listener - callback that informs that keyboard is shown. Useful  for
 * animations or complex handles for "adjustResize"
 */
@BindingAdapter("forceKeyboard")
fun EditText.forceKeyboardResize(listener: (() -> Unit)? = null) {
    //it is bind before view is fully initialised, thus  we need to introduce small delay
    postDelayed({
        val receiver: ResultReceiver? = if (listener != null) {
            object : ResultReceiver(null) {
                override fun onReceiveResult(
                    resultCode: Int,
                    resultData: Bundle?
                ) {
                    if (resultCode == InputMethodManager.RESULT_SHOWN || resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN) {
                        postDelayed({
                            listener.invoke()
                        }, 300)
                    }
                }
            }
        } else {
            null
        }
        requestFocus()
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            this,
            InputMethodManager.SHOW_FORCED,
            receiver
        )
    }, 300)
}

fun EditText.forceKeyboardPan() {
    //it is bind before view is fully initialised, thus  we need to introduce small delay
    postDelayed({
        requestFocus()
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }, 300)
}

fun EditText.textFlow(): Flow<String> = callbackFlow {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(s?.toString() ?: "")
        }
    }
    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}

fun Context.copyTextToClipboard(textToCopy: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", textToCopy)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
}


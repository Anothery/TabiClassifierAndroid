package com.sudzusama.vkimageclassifier.utils.ext

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })
    return query
}

fun Context.shortToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.removeGravity() =
    this.updateLayoutParams<FrameLayout.LayoutParams> { gravity = Gravity.NO_GRAVITY }

fun View.addGravity(g: Int) = this.updateLayoutParams<FrameLayout.LayoutParams> { gravity = g }

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Int.toDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()
val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

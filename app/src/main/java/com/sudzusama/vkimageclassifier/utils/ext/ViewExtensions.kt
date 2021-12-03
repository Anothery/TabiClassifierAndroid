package com.sudzusama.vkimageclassifier.utils.ext

import android.app.Activity
import android.content.Context
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

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showShortMessage(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()


fun Fragment.showShortMessage(text: String) = activity?.showShortMessage(text)

fun View.removeGravity() = this.updateLayoutParams<FrameLayout.LayoutParams> { gravity = Gravity.NO_GRAVITY }
fun View.addGravity(g: Int) = this.updateLayoutParams<FrameLayout.LayoutParams> { gravity = g }

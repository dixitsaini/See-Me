package com.example.seeme.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.seeme.activity.BaseActivity

abstract class BaseFragment : Fragment() {

    /**
     * Hides the software keyboard.
     */
    protected fun hideKeyboard(view: View? = activity?.currentFocus) {
        (activity as? BaseActivity)?.hideKeyboard(view)
    }

    /**
     * Shows the software keyboard and focuses the given view.
     */
    protected fun showKeyboard(view: View) {
        (activity as? BaseActivity)?.showKeyboard(view)
    }

    /**
     * Provides access to the BaseActivity.
     */
    protected val baseActivity: BaseActivity?
        get() = activity as? BaseActivity
}

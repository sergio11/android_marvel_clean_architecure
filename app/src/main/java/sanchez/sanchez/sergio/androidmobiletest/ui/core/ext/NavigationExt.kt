package sanchez.sanchez.sergio.androidmobiletest.ui.core.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

/**
 * Navigation Extensions
 */

/**
 * Navigate
 * @param navDirections
 */
fun Fragment.navigate(navDirections: NavDirections) {
    NavHostFragment.findNavController(this).navigate(navDirections)
}

fun Fragment.popBackStack(@IdRes idFragmentRes: Int) {
    NavHostFragment.findNavController(this).popBackStack(idFragmentRes, true)
}

fun Fragment.popBackStack() {
    NavHostFragment.findNavController(this).popBackStack()
}
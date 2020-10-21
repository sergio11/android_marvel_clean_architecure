package sanchez.sanchez.sergio.androidmobiletest.ui.core.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

/**
 * Navigation Extensions
 */

/**
 * Navigate
 * @param action
 * @param args
 */
fun Fragment.navigate(@IdRes action: Int, args: Bundle? = null) {
    val navController = NavHostFragment.findNavController(this)
    navController.currentDestination?.getAction(action)?.let {
        navController.navigate(action, args)
    }
}

/**
 * Navigate
 * @param navDirections
 */
fun Fragment.navigate(navDirections: NavDirections) {
    NavHostFragment.findNavController(this).navigate(navDirections)
}

/**
 *
 */
fun Fragment.navigate(destination: ActivityNavigator.Destination) {
    ActivityNavigator(context!!).navigate(
        destination, null, null, null
    )
}

fun Fragment.popBackStack(@IdRes idFragmentRes: Int) {
    NavHostFragment.findNavController(this).popBackStack(idFragmentRes, true)
}

fun Fragment.popBackStack() {
    NavHostFragment.findNavController(this).popBackStack()
}
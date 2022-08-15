package io.novafoundation.nova.common.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.findSuitableParent

class CustomSnack(
    parent: ViewGroup,
    content: CustomSnackView
) : BaseTransientBottomBar<CustomSnack>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(view: View, isError: Boolean, message: String, listener: View.OnClickListener? = null): CustomSnack? {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            try {
//                val inflated = LayoutInflater.from(view.context).inflate(R.layout.view_custom_snack, parent, false)
                val customView = CustomSnackView(view.context)

                // We create and return our Snackbar
                customView.setText(isError, message)

                return CustomSnack(parent, customView).setDuration(4000)
            } catch (e: Exception) {
                Log.v("exception ", e.message.toString())
            }

            return null
        }

    }
}

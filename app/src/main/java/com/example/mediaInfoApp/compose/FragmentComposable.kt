package com.example.mediaInfoApp.compose

import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@Composable
 fun FragmentComposable(supportFragmentManager : FragmentManager, fragment: Fragment, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()
                setBackgroundColor(Color(0xFFFAFAFA).toArgb())
                post {
                    if (!fragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .add(id, fragment)
                            .commitNow()
                    }
                }
            }
        },
        modifier = modifier
    )
}
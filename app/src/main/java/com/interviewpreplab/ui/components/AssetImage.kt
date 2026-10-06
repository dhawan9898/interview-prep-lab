package com.interviewpreplab.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Decodes a bundled image (webp, png, jpg) off the main thread and draws it at full width. Draws nothing if it cannot load. */
@Composable
fun AssetImage(path: String, contentDescription: String?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(null, path) {
        value = withContext(Dispatchers.IO) {
            try {
                context.assets.open(path).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            } catch (e: java.io.IOException) {
                null
            }
        }
    }
    bitmap?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            // The artwork is drawn for a white page, so keep it on white in dark mode too.
            modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.White)
        )
    }
}

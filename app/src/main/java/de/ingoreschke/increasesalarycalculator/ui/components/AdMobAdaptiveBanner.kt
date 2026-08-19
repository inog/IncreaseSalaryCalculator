package de.ingoreschke.increasesalarycalculator.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdMobAdaptiveBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-1283865206002218/3244437360"
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth)
                setAdSize(adSize)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

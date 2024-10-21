package de.ingoreschke.increasesalarycalculator

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import de.ingoreschke.increasesalarycalculator.ui.theme.IncreaseSalaryCalculatorTheme
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    private lateinit var presenter: SalaryIncreasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get access to shared preferences
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Retrieve previously saved values if any
        val lastSalary = BigDecimal(prefs.getString("last_salary", "0.0"))
        val lastIncrease = BigDecimal(prefs.getString("last_increase", "0.0"))


        MobileAds.initialize(this) {}
        presenter = SalaryIncreasePresenter(SalaryIncreaseInteractor())

        setContent {
            IncreaseSalaryCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Intro()
                        Spacer(modifier = Modifier.height(44.dp))
                        SalaryIncrease(presenter = presenter, lastSalary = lastSalary, lastIncrease = lastIncrease)
                        AdMobBanner()
                    }
                }
            }
        }
    }
}


@Composable
fun Intro(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            modifier = modifier
        )
        Text(
            text = stringResource(id = R.string.introtext),
            fontSize = 16.sp,
            modifier = modifier
        )
        Text(
            text = stringResource(id = R.string.introtext2),
            modifier = modifier
        )
    }
}

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)
                adUnitId = "ca-app-pub-1283865206002218/3244437360"
                //adUnitId = "ca-app-pub-3940256099942544/6300978111" // test ad
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IncreaseSalaryCalculatorTheme {
        Surface {
            Intro()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdMobBannerPreview() {
    IncreaseSalaryCalculatorTheme {
        Surface {
            AdMobBanner()
        }
    }
}
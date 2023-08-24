package nl.vanparerensoftwaredevelopment.saltedpassmanagement.android

import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import nl.vanparerensoftwaredevelopment.saltedpassmanagement.common.ui.windowsize.withLocalWindowSize

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withLocalWindowSize(getWindowSize(this)) {
                MaterialTheme {
                    App()
                }
            }
        }
    }
}
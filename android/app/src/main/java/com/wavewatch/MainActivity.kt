package com.wavewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.wavewatch.ui.navigation.WaveWatchNavHost
import com.wavewatch.ui.theme.WaveWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            WaveWatchTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WaveWatchNavHost()
                }
            }
        }
    }
}

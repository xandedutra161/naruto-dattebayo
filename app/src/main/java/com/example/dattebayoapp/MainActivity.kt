package com.example.dattebayoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dattebayoapp.navigation.DattebayoNavigation
import com.example.dattebayoapp.ui.theme.DattebayoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DattebayoAppTheme {
                DattebayoNavigation()
            }
        }
    }
}

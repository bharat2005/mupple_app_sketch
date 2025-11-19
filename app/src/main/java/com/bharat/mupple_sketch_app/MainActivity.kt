package com.bharat.mupple_sketch_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bharat.mupple_sketch_app.app_root.AppRoot
import com.bharat.mupple_sketch_app.app_root.AppRootViewModel
import com.bharat.mupple_sketch_app.app_root.NetworkMonitor
import com.bharat.mupple_sketch_app.core.data.repo.AuthState
import com.bharat.mupple_sketch_app.ui.theme.Mupple_sketch_appTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel : AppRootViewModel by viewModels()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewmodel.authState.value == AuthState.UNKNOWN
        }
        enableEdgeToEdge()

            setContent {
                Mupple_sketch_appTheme {
                    AppRoot()
                }
            }



    }
}


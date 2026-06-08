package ro.subcontrol.decizii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ro.subcontrol.decizii.ui.DecisionScreen
import ro.subcontrol.decizii.ui.theme.SubcontrolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SubcontrolTheme {
                DecisionScreen()
            }
        }
    }
}

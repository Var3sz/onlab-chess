package hu.bme.aut.android.monkeychess.splashScreen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.R
import kotlinx.coroutines.delay

class SplashScreenUI : ComponentActivity(){

    @Composable
    fun SplashScreen(navController: NavController){
        LaunchedEffect(key1 = true){
            delay(2500L)
            navController.navigate("welcome_screen")
        }

        /** Splash screen beállítása  **/
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
            Image(painter = painterResource(id = R.drawable.splash_screen), contentDescription = "Logo")
        }
    }
}
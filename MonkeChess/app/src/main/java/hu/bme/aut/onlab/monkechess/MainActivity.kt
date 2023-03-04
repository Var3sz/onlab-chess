package hu.bme.aut.onlab.monkechess

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toolbar
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.onlab.monkechess.ui.theme.MonkeChessTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonkeChessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    MainActivityNavigation()
                }
            }
        }
    }

    /* TODO: Visszagomb visszairányít a Splash Screen-re, ami nem az igazi */
    /**
     * MainActivity navigációját megvalósító függvény
     * */
    @Composable
    fun MainActivityNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash_screen"){
            composable("splash_screen"){
                SplashScreen(navController)
            }
            composable("welcome_screen"){
                TopAppBarWidget()
            }
        }
    }

    /**
     * Splash Screen megvalósítása
     */
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

    /**
     * TopAppBar
     * */
    @Composable
    fun TopAppBarWidget() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "MonkeyChess",
                            color = MaterialTheme.colors.background
                        )
                    },
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = MaterialTheme.colors.background,
                )
            }, content = {
                Text("Hello MonkeyChess")
            })
    }

}


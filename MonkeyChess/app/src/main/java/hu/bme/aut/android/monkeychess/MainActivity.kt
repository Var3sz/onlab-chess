package hu.bme.aut.android.monkeychess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.monkeychess.WelcomeScreen.WelcomeUI
import hu.bme.aut.android.monkeychess.Login.LoginUI
import hu.bme.aut.android.monkeychess.Register.RegisterUI
import hu.bme.aut.android.monkeychess.SplashScreen.SplashScreenUI
import hu.bme.aut.android.monkeychess.ui.theme.MonkeChessTheme

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
    override fun onBackPressed() {
        //finish()
    }

    @Composable
    fun MainActivityNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash_screen"){
            composable("splash_screen"){
                //SplashScreenUI().SplashScreen(navController)
                TopAppBarWidget(navController = navController, content =  { SplashScreenUI().SplashScreen(navController)})
            }
            composable("welcome_screen"){
                TopAppBarWidget(navController = navController, content =  { WelcomeUI().WelcomeScreenContent(navController)}, bottomBar = {WelcomeUI().WelcomeScreenButtons(navController)})
            }

            composable("login_screen"){
               //LoginUI().LoginScreenContent()
                TopAppBarWidget(navController = navController, content =  { LoginUI().LoginScreenContent()},bottomBar = {LoginUI().LoginButton()} )
            }
            composable("register_screen"){
                TopAppBarWidget(navController = navController, content = { RegisterUI().RegisterScreenContent()}, bottomBar = {RegisterUI().RegisterButton()})
            }
        }
    }


    /**
     * TopAppBar
     * */
    @Composable
    fun TopAppBarWidget(navController: NavController, content: @Composable () -> Unit={}, bottomBar: @Composable () -> Unit={} ) {
        Scaffold(
            topBar = {WelcomeScreenTopBar() },
            content = {content()},
            bottomBar = {bottomBar()}
        )
    }

    @Composable
    fun WelcomeScreenTopBar(){
        TopAppBar(
            title = {
                Text(
                    text = "MonkeyChess",
                    color = MaterialTheme.colors.background
                )
            },
            backgroundColor = MaterialTheme.colors.onBackground,
            contentColor = MaterialTheme.colors.background
        )
    }


}


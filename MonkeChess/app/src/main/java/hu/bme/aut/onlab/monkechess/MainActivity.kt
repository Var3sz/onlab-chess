package hu.bme.aut.onlab.monkechess

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.onlab.monkechess.ui.theme.MonkeChessTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import hu.bme.aut.onlab.monkechess.Login.Login
import hu.bme.aut.onlab.monkechess.Login.LoginUI

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
        finish()
    }

    @Composable
    fun MainActivityNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash_screen"){
            composable("splash_screen"){
                SplashScreen(navController)
            }
            composable("welcome_screen"){
                TopAppBarWidget(navController)
            }

            composable("login_screen"){
                LoginUI().LoginActivityNavigation()
            }

            composable("register_screen"){

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
    fun TopAppBarWidget(navController: NavController) {
        Scaffold(
            topBar = {WelcomeScreenTopBar() },
            content = { WelcomeScreenContent(navController)}
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

    @Composable
    fun WelcomeScreenContent(navController: NavController){
        val mContext = LocalContext.current
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement =  Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

            Box(contentAlignment = Alignment.Center,  modifier = Modifier.padding(100.dp)){
                Image(painter = painterResource(id = R.drawable.splash_screen), contentDescription = "Logo",)
                //Text("demo")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment =  Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                //login button
                OutlinedButton(
                    modifier = Modifier.width(100.dp),
                    onClick = {navController.navigate("login_screen")},
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Login")
                }

                //register button
                OutlinedButton(
                    modifier = Modifier.width(100.dp),
                    onClick = { navController.navigate("register_screen")

                              },
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Register")
                }
            }
        }
    }
}


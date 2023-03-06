package hu.bme.aut.onlab.monkechess

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.internal.TextScale
import hu.bme.aut.onlab.monkechess.ui.theme.MonkeChessTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("err", "started")
        super.onCreate(savedInstanceState)
        setContent {
            MonkeChessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginActivityNavigation()
                }
            }
        }
    }

    @Composable
    fun LoginActivityNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login_screen"){

            composable("login_screen"){
                TopAppBarWidget()
            }
        }
    }


    @Preview
    @Composable
    fun TopAppBarWidget() {
        Scaffold(
            topBar = {LoginScreenTopBar() },
            content = { LoginScreenContent()}
        )
    }

    @Composable
    fun LoginScreenTopBar(){
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
    fun LoginScreenContent(){
        Column(modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement =  Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = "Welcome back!",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )


        }
    }
}

package hu.bme.aut.android.monkeychess.welcomeScreen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.R

class WelcomeUI : ComponentActivity() {

    @Composable
    fun WelcomeScreenContent(){
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement =  Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

            Box(contentAlignment = Alignment.Center){
                Image(painter = painterResource(id = R.drawable.splash_screen), contentDescription = "Logo",
                modifier = Modifier.height(400.dp).width(400.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment =  Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {}
        }
    }

    @Composable
    fun WelcomeScreenButtons(navController: NavController){
        Row {
            Spacer(modifier = Modifier.width(48.dp))

            OutlinedButton(
                modifier = Modifier.width(100.dp),
                onClick = {navController.navigate("login_screen")},
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.weight(1f))

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

            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}
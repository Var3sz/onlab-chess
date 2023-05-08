package hu.bme.aut.android.monkeychess.mainMenu

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.R

class MainMenuUI {

    //@Preview
    @Composable
    fun MainMenu(navController: NavController, viewModel: MainMenuViewModel){
        val usernameLiveData by viewModel.getUsername().observeAsState()
        val context = LocalContext.current

        Row{
            OutlinedButton(
                onClick = {
                          viewModel.logoutUser(context, navController)
                },
                border = BorderStroke(0.dp, Color.White),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = CircleShape
            ) {
                Image(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = "Logout icon",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Column{
                Spacer(modifier = Modifier.height(18.dp))
                Text(usernameLiveData?:"")
            }
            OutlinedButton(
                onClick = {
                    navController.navigate("profile_screen")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(0.dp, Color.White),
                shape = CircleShape
            ) {
                Image(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "Profile icon",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                )
            }
        }


        Column( modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ){
            //Single Player button
            MainButtons(text = "Single Player", click =  {Log.d("MainMenu", " Single Player clicked" )
                navController.navigate("board_screen")
            })
            //Multi Player button
            MainButtons(text = "Multi Player", click =  {Log.d("MainMenu", " Multi Player clicked" )
                navController.navigate("board_1v1")})
            //Puzzles button
            MainButtons(text = "Puzzles",  click = {Log.d("MainMenu", " Puzzles clicked" )})
            //Friends button
            MainButtons(text = "Friends", click =  {Log.d("MainMenu", " Friends clicked" )})
            //Stats button
            MainButtons(text = "Stats", click =  {Log.d("MainMenu", " Stats clicked" )} )
        }
    }

    @Composable
    fun MainButtons(text: String, click: () -> Unit={}){
        OutlinedButton(
            modifier = Modifier
                .width(200.dp)
                .padding(12.dp)
            ,
            onClick = {click()},
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Text(text)
        }
    }


}
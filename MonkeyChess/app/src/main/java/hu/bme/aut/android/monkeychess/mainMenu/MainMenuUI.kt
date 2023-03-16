package hu.bme.aut.android.monkeychess.mainMenu

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.R

class MainMenuUI {

    //@Preview
    @Composable
    fun MainMenu(navController: NavController){
        Row{
            //Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = { /*TODO*/ },
                border = BorderStroke(0.dp, Color.White),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = CircleShape
            ) {
                Image(painter = painterResource(id = R.drawable.baseline_logout_24), contentDescription = "Logout icon",
                    modifier = Modifier.width(40.dp).height(40.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(0.dp, Color.White),
                shape = CircleShape
            ) {
                Image(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "Profile icon",
                    modifier = Modifier.width(40.dp).height(40.dp),
                )
            }
            //Spacer(modifier = Modifier.width(12.dp))
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
            MainButtons(text = "Multi Player", click =  {Log.d("MainMenu", " Multi Player clicked" )})
            //Puzzles button
            MainButtons(text = "Puzzles",  click = {Log.d("MainMenu", " Puzzles clicked" )})
            //Friends button
            MainButtons(text = "Friends", click =  {Log.d("MainMenu", " Friends clicked" )})
            //Stats button
            MainButtons(text = "Stats", click =  {Log.d("MainMenu", " Stats clicked" )} )

            /** TODO: Profile button átmeneti megoldás **/
            MainButtons(text = "Profile", click = {Log.d("MainMenu", "Profile clicked")
                navController.navigate("profile_screen")
            })
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
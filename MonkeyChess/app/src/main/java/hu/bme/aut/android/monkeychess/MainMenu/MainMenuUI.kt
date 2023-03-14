package hu.bme.aut.android.monkeychess.MainMenu

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainMenuUI {

    @Preview
    @Composable
    fun MainMenu(){
        Column( modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ){
            //Single Player button

            MainButtons(text = "Single Player")
            //Multi Player button
            MainButtons(text = "Multi Player")
            //Puzzles button
            MainButtons(text = "Puzzles")
            //Friends button
            MainButtons(text = "Friends")
            //Stats button
            MainButtons(text = "Stats")

        }
    }

    @Composable
    fun MainButtons(text: String){
        OutlinedButton(
            modifier = Modifier
                .width(200.dp)
                .padding(12.dp)
            ,
            onClick = {
                //Log.d("TODO", "password: ${password} email= ${email}")
                TODO()
            },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Text(text)
        }
    }


}
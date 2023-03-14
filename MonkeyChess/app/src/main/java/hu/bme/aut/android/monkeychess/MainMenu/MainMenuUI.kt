package hu.bme.aut.android.monkeychess.MainMenu

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainMenuUI {

    @Preview
    @Composable
    fun MainMenu(){
        Box( modifier = Modifier.fillMaxSize()){
            OutlinedButton(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth(),
                onClick = {
                    //Log.d("TODO", "password: ${password} email= ${email}")
                    TODO()
                },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            ) {
                Text("Login")
            }
        }
    }

}
package hu.bme.aut.android.monkeychess.profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.monkeychess.R
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


class ProfileUI {

    @Composable
    fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
        val usernameLiveData by viewModel.getUsername().observeAsState()
        val fullnameLiveData by viewModel.getFullname().observeAsState()
        val emailLiveData by viewModel.getEmail().observeAsState()
        val accCreatedAtLiveData by viewModel.getAccCreatedAt().observeAsState()
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "My Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(300.dp)
                    .clip(CircleShape)
            )
            Text(
                text=usernameLiveData?:"",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(fullnameLiveData?:"")
            Spacer(modifier = Modifier.height(15.dp))
            Text(emailLiveData?:"")
            Spacer(modifier = Modifier.height(15.dp))
            Text(accCreatedAtLiveData?:"")
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate("delete_user")
                },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            ) {
                Text("Delete account")
            }

        }
    }
}
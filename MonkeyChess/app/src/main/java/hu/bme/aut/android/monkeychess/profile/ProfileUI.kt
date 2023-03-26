package hu.bme.aut.android.monkeychess.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.monkeychess.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


class ProfileUI {

    @Composable
    fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
        var expanded by remember { mutableStateOf(false) }
        val usernameLiveData by viewModel.getUsername().observeAsState()
        val fullnameLiveData by viewModel.getFullname().observeAsState()
        val emailLiveData by viewModel.getEmail().observeAsState()
        val accCreatedAtLiveData by viewModel.getAccCreatedAt().observeAsState()

        Box(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(TopEnd))
        {
            OutlinedButton(
                onClick = {
                   expanded = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(0.dp, Color.White),
                shape = CircleShape
            ) {
                Image(painter = painterResource(id = R.drawable.baseline_menu_24), contentDescription = "Profile icon",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                   onClick = {expanded = false},
                    modifier = Modifier.wrapContentSize()
                ){
                    Text(text = "Change profile")
                }
                Divider()
                DropdownMenuItem(
                    onClick = {navController.navigate("change_email")},
                    modifier = Modifier.wrapContentSize()
                ){
                    Text(text = "Change e-mail address")
                }
                Divider()
                DropdownMenuItem(
                    onClick = {navController.navigate("change_password")},
                    modifier = Modifier.wrapContentSize()
                ){
                    Text(text = "Change password")
                }
            }
        }


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
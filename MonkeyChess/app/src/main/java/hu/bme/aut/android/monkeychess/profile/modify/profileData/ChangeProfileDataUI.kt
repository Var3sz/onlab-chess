package hu.bme.aut.android.monkeychess.profile.modify.profileData

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

class ChangeProfileDataUI {

    @Composable
    fun ChangeProfileDataScreen(viewModel: ChangeProfileDataViewModel, navController: NavController){
        val newUsernameState = remember { mutableStateOf("") }
        val newFullnameState = remember { mutableStateOf("") }
        val context = LocalContext.current


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 48.dp)
                        .fillMaxWidth(),
                    text = "Modify username or fullname",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "New username"
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newUsernameState.value,
                    onValueChange = { typed -> newUsernameState.value = typed
                        viewModel.setNewUsername(newUsernameState.value)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    label = { Text(text = "Your new username")}
                )
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = "New fullname",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newFullnameState.value,
                    onValueChange = {typed -> newFullnameState.value = typed
                        viewModel.setNewFullname(newFullnameState.value) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text(text = "Your new fullname")}
                )
                Spacer(modifier = Modifier.height(30.dp))
                OutlinedButton(
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        viewModel.isChangeProfileDataInputValid(context, navController)
                    },
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Modify")
                }
            }

        }
    }


}
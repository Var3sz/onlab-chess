package hu.bme.aut.android.monkeychess.Register

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

class RegisterUI {
    @Composable
    fun RegisterScreenContent(viewModel: RegisterViewModel){
        val fullnameState = remember { mutableStateOf("") }
        val emailState = remember { mutableStateOf("") }
        val usernameState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val confirmpasswordState = remember { mutableStateOf("") }

        Box( modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 48.dp)
                        .fillMaxWidth(),
                    text = "Create an account!",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Fullname",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = fullnameState.value,
                    onValueChange = { typed -> fullnameState.value = typed
                        viewModel.settFullname(fullnameState.value) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text(text = "Your fullname") }
                )

                //Email field
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = "E-mail",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailState.value,
                    onValueChange = { typed -> emailState.value = typed
                        viewModel.setEmail(emailState.value) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    label = { Text(text = "Your e-mail address") }
                )

                //Username field
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = "Username",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = usernameState.value,
                    onValueChange = { typed -> usernameState.value = typed
                        viewModel.setUsername(usernameState.value) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    label = { Text(text = "Your username") }
                )

                //Password field
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = "Password",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordState.value,
                    onValueChange = {typed -> passwordState.value = typed
                        viewModel.setPassword(passwordState.value)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    label = { Text(text = "Your password") },
                    visualTransformation = PasswordVisualTransformation()
                )

                //Confirm password field
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = "Confirm password",
                    textAlign = TextAlign.Start,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmpasswordState.value,
                    onValueChange = {typed -> confirmpasswordState.value = typed
                        viewModel.setConfirmpassword(confirmpasswordState.value)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    label = { Text(text = "Confirm your password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }
    }


    @Composable
    fun RegisterButton(navController: NavController, viewModel: RegisterViewModel){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {

            OutlinedButton(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth(),
                onClick = {
                    Log.d("TODO", "pass: ${viewModel.getPassword()} email: ${viewModel.getEmail()}")
                    viewModel.registerUser(viewModel.getEmail().toString(), viewModel.getPassword().toString())
                    navController.navigate("login_screen")
                },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            ) {
                Text("Register")
            }
        }
    }
}
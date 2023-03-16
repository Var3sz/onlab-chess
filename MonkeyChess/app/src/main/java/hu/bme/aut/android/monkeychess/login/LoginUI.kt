package hu.bme.aut.android.monkeychess.login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import hu.bme.aut.android.monkeychess.login.LoginViewModel

public class LoginUI{
    @Composable
    fun LoginScreenContent(viewModel: LoginViewModel){
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        Box( modifier = Modifier.fillMaxSize()) {
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
                        text = "Welcome back!",
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        )

                //email field
                Text(
                        text = "E-mail",
                        textAlign = TextAlign.Start,
                        )
                OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailState.value,
                        onValueChange = { typed -> emailState.value = typed
                                viewModel.setEmail(emailState.value)
                                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email
                        ),
                        label = { Text(text = "Your e-mail address")}
                )

                // password field
                Text(
                        modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
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
                        label = { Text(text = "Your password")},
                        visualTransformation = PasswordVisualTransformation()
                )
            }
        }
    }

    @Composable
    fun LoginButton(navController: NavController, viewModel: LoginViewModel){
        val context = LocalContext.current
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
                        //viewModel.isLoginInputValid(context, navController)
                        navController.navigate("MainMenu_screen")
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

package hu.bme.aut.android.monkeychess.Login

import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

public class LoginUI : ComponentActivity(){
    private var password= String()
    private  var email= String()



    @Composable
    fun LoginScreenContent(){
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

                //Field("Your e-mail address")
                //email field
                Text(
                        text = "E-mail",
                        textAlign = TextAlign.Start,
                        )
                OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailState.value,
                        onValueChange = { typed -> emailState.value = typed
                                email= emailState.value },
                        keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email
                        ),
                        label = { Text(text = "Your e-mail address")}
                )

                // password field
                Text(
                        text = "Password",
                        textAlign = TextAlign.Start,
                        )
                OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordState.value,
                        onValueChange = {typed -> passwordState.value = typed
                                password=passwordState.value
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password
                        ),
                        label = { Text(text = "password")},
                        visualTransformation = PasswordVisualTransformation()
                )
            }
        }
    }

    @Composable
    fun LoginButton(){
        Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
        ) {

            OutlinedButton(
                    modifier = Modifier
                            .width(100.dp)
                            .fillMaxWidth(),
                    onClick = {
                            Log.d("TODO", "password: ${password} email= ${email}")
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
    @Composable
    fun Field(name: String,){
        OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "Your e-mail address",
                onValueChange = {registration()},
                keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                )
        )
    }

    fun registration(){

    }

}

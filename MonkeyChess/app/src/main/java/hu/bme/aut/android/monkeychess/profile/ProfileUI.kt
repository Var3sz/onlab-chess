package hu.bme.aut.android.monkeychess.profile

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.monkeychess.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest


class ProfileUI {

    @Composable
    fun ProfileScreen(viewModel: ProfileViewModel, navController: NavController) {
        var expanded by remember { mutableStateOf(false) }
        val usernameLiveData by viewModel.getUsername().observeAsState()
        val fullnameLiveData by viewModel.getFullname().observeAsState()
        val emailLiveData by viewModel.getEmail().observeAsState()
        val accCreatedAtLiveData by viewModel.getAccCreatedAt().observeAsState()
        var showDeletePopUp by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val imageURL by viewModel.getImageUrl().observeAsState()
        val pickImage = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri ->
            viewModel.uploadPicture(uri, context)
        }

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
                   onClick = { navController.navigate("change_profile_data") },
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
            Spacer(Modifier.height(30.dp))
            OutlinedButton(
                onClick = { pickImage.launch("image/*") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(0.dp, Color.White),
                shape = CircleShape
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageURL)
                        .build(),
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(360.dp)
                )
            }

            Text(
                text=usernameLiveData?:"",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(fullnameLiveData?:"")
            Spacer(modifier = Modifier.height(15.dp))
            Text(emailLiveData?:"")
            Spacer(modifier = Modifier.height(15.dp))
            Text((accCreatedAtLiveData?:""))
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxWidth(),
                onClick = {
                    showDeletePopUp = true
                },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            ) {
                Text("Delete account")
            }
            
            DeletePopUp(viewModel, isOpen = showDeletePopUp, onDismiss = {showDeletePopUp = false}, navController = navController, context = context)

        }
    }

    @Composable
    fun DeletePopUp(viewModel: ProfileViewModel, isOpen: Boolean, onDismiss: () -> Unit, navController: NavController, context: Context){
        val passwordState = remember { mutableStateOf("") }
        val emailState = remember { mutableStateOf("") }
        if (isOpen) {
            AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                        Column(
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Black,
                                text = "Delete Account!",
                                textAlign = TextAlign.Center,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Black,
                                text = "E-mail",
                                textAlign = TextAlign.Start,
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = emailState.value,
                                onValueChange = { typed -> emailState.value = typed
                                    viewModel.reAuthEmail(emailState.value)
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Email
                                ),
                                label = { Text(text = "Your e-mail address")}
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Black,
                                text = "Password",
                                textAlign = TextAlign.Start,
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(),
                                value = passwordState.value,
                                onValueChange = {typed -> passwordState.value = typed
                                    viewModel.reAuthPassword(passwordState.value)},
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Password
                                ),
                                label = { Text(text = "Your password")},
                                visualTransformation = PasswordVisualTransformation()
                            )
                        }
                       },
                 buttons = {
                     Box(
                         modifier = Modifier.fillMaxWidth(),
                         contentAlignment = Center
                     ) {
                         OutlinedButton(
                             modifier = Modifier.width(100.dp),
                             onClick = { viewModel.isDeleteInputValid(context, navController) },
                             border = BorderStroke(1.dp, Color.Black),
                             shape = RoundedCornerShape(50),
                             colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                         ) {
                             Text("Delete")
                         }
                     }
                 }
            )
        }
    }
}
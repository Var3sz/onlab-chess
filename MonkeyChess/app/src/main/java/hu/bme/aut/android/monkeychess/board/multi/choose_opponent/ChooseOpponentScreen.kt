package hu.bme.aut.android.monkeychess.board.multi.choose_opponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun ChooseOpponentScreen(navController: NavController, viewModel: ChooseOpponentViewModel){
    val users: List<Pair<String, String>> by viewModel.users.observeAsState(emptyList())
    val currentUser: String? by viewModel.currentUser.observeAsState()


    Column(modifier = Modifier.fillMaxSize()){
        if(users.isNullOrEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No other users available!",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        else{
            LazyColumn() {
                items(users) { user ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .border(1.dp, Color.Gray)
                                .height(64.dp)
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate("new_multiplayer_game/${currentUser.toString()}/${user.first}")
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(user.second)
                                        .build(),
                                    loading = {
                                        CircularProgressIndicator()
                                    },
                                    contentDescription = "Profile picture",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(24.dp)
                                )
                                Text(
                                    text = user.first,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package hu.bme.aut.android.monkeychess.board.multi.select_game

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.R
import hu.bme.aut.android.monkeychess.board.multi.Games

@Composable
fun SelectGameScreen(navController: NavController, viewModel: SelectGameViewModel){
    val games: List<Games> by viewModel.games.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray,
            border = BorderStroke(1.dp, Color.Gray),
            elevation = 1.dp,
            shape = RectangleShape
        ) {
            Button(
                onClick = {
                    navController.navigate("choose_opponent")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                ),
                elevation = ButtonDefaults.elevation(0.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(text = "New Game",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold)
            }
        }
        if(games.isNullOrEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No games available!",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }else{
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(games) { game ->
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
                                    navController.navigate("load_multiplayer_game/${game.playerOne}/${game.playerTwo}/${game.gameID}/${Uri.encode(game.fen)}")
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.black_queen),
                                    contentDescription = "Game Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "${game.playerOne} vs ${game.playerTwo}",
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
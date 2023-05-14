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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import hu.bme.aut.android.monkeychess.R
import androidx.compose.foundation.lazy.items

@Composable
fun ChooseOpponentScreen(viewModel: ChooseOpponentViewModel){
    val userData by viewModel.getUsers().observeAsState()
    val users = userData ?: emptyList()

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
                        .clickable { /* Handle click here */ }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "Person Icon",
                        )
                        Text(
                            text = user,
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
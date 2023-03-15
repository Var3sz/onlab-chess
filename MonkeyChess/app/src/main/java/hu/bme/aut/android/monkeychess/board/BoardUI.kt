package hu.bme.aut.android.monkeychess.board

import android.media.Image
import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.monkeychess.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState


import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.monkeychess.ui.theme.Shapes


class BoardUI {


    @Composable
    fun GameScreen(playerOne: String = "Alice", playerTwo: String = "Bob", viewModel :BoardViewModel) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width((8 * 45).dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {

                //PlayerOne
                Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    DrawPalyer(playerOne)
                }

                //ChessBoard
                Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    DrawBoard(viewModel)
                }

                //PlayerTwo
                Row(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    DrawPalyer(playerTwo)
                }
            }
        }
    }


    @Composable
    fun DrawPalyer(name: String = "PlayerName") {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.narkos),
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )



            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
    }


    @Composable
    fun DrawBoard(viewModel :BoardViewModel) {
        //val matrixdata by viewModel.matrixLiveData.observeAsState(emptyList<List<Boolean>>())

        Column(
        ) {
            for (i in 0 until 8) {
                Row {
                    for (j in 0 until 8) {
                        var gridcolor = Color.Black
                        if ((i + j) % 2 == 0) {
                            gridcolor = Color.White
                        }
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(gridcolor)
                                .clickable {
                                    Log.d(
                                        "Board",
                                        "i: ${i}, j: ${j} board value: "
                                    )
                                    viewModel.matrixLiveData.value
                                    viewModel.setValue(i, j, true)

                                },
                            contentAlignment = Alignment.Center,

                        ) {
                            if(viewModel.getValue(i,j) == true)
                                DrawCircle()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DrawCircle() {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawCircle(
                color = Color(0xff0f9d58),
                center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                radius = size.minDimension / 2,
                style = Stroke(10F)
            )
        }

    }
}
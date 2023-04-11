package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.monkeychess.R
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.monkeychess.board.pieces.Piece
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor

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
        //val piecesLiveData by viewModel.piecesLiveData.observeAsState(emptyList<List<Piece>>())
        val tilesLiveData by viewModel.tilesLiveData.observeAsState(emptyList<List<Piece>>())

        Column(
        ) {
            for (i in 0 until 8) {
                Row {
                    for (j in 0 until 8) {
                        var gridcolor = Color.Gray
                        if ((i + j) % 2 == 0) {
                            gridcolor = Color.White
                        }
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(gridcolor)
                                .clickable {
                                    if(viewModel.getValue(i,j) == true) {
                                        viewModel.HideAvailableSteps()
                                        viewModel.step(viewModel.getClickedPiece(), i, j)
                                    }
                                    else{
                                        viewModel.HideAvailableSteps()
                                        viewModel.setClickedPiece(viewModel.getPiece(i, j))

                                        Log.d(
                                            "Board1",
                                            "i: ${i}, j: ${j} board value: ${
                                                viewModel.getValue(
                                                    i,
                                                    j
                                                )
                                            } babu:${viewModel.getPiece(i, j).pieceColor} "
                                        )

                                        if (viewModel.getPiece(i, j).pieceColor != PieceColor.EMPTY) {
                                            val piece = viewModel.getPiece(i, j)
                                            val steps = viewModel.getAvailableSteps(piece)

                                            Log.d(
                                                "BoardStep",
                                                "i: ${piece.i}, j: ${piece.j} "
                                            )
                                            steps.forEach() {
                                                viewModel.setValue(it.first, it.second, true)
                                                //viewModel.tilesLiveData.value?.get(it.first)?.get(it.second)?.free=true
                                                Log.d(
                                                    "BoardStep",
                                                    "i: ${it.first}, j: ${it.second} } "
                                                )
                                            }
                                        }
                                    }
                                    //viewModel.matrixLiveData.value
                                },
                            contentAlignment = Alignment.Center,

                        ) {
                            /*
                            Log.d(
                                "Board2",
                                "i: ${i}, j: ${j} board value: ${viewModel.getValue(i,j)} babu:${viewModel.getPiece(i,j)!!.name }"
                            )

                             */

                            if(viewModel.getValue(i,j) == true ){
                                DrawCircle()
                            }


                            if(viewModel.getPiece(i,j).pieceColor != PieceColor.EMPTY)
                                DrawPiece(viewModel.getPiece(i,j).imageID)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DrawPiece(imageID: Int) {
        Image(
            painter = painterResource(id = imageID),
            contentDescription = "profile picture"
        )
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
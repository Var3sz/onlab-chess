package hu.bme.aut.android.monkeychess.board

import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hu.bme.aut.android.monkeychess.board.pieces.Piece
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName
import hu.bme.aut.android.monkeychess.board.pieces.enums.Side
import okhttp3.internal.connection.Exchange

class BoardUI() {
    @Composable
    fun GameScreen(playerOne: String = "Alice", playerTwo: String = "Bob", viewModel :BoardViewModel) {
        val whiteExchange by viewModel.getWhiteExchangeState().observeAsState()
        val blackExchange by viewModel.getBlackExchangeState().observeAsState()
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
                    DrawPlayer(playerOne)
                }

                //ChessBoard
                Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    DrawBoard(viewModel)
                }

                //PlayerTwo
                Row(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    DrawPlayer(playerTwo)
                }
                Button(onClick = { viewModel.board.value?.FlipTheTable() }) {
                    Text(text = "flipy flopity\nyou are my flipity ")
                }
            }
            ExchangePieceAlert(
                viewModel = viewModel,
                isWhiteExchange = whiteExchange!!,
                isBlackExchange = blackExchange!!,
                onDismiss = { }
            )
        }
    }

    @Composable
    fun DrawPlayer(name: String = "PlayerName") {
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
        //val tilesLiveData by viewModel.tilesLiveData.observeAsState(emptyList<List<Piece>>())

        Column {
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

                                    if (viewModel.getValue(i, j) == true) {
                                        viewModel.HideAvailableSteps()
                                        viewModel.step(viewModel.getClickedPiece(), i, j)
                                        // viewModel.ChangeCurrentPlayer()
                                    } else {
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

                                        if (viewModel.getPiece(
                                                i,
                                                j
                                            ).pieceColor != PieceColor.EMPTY
                                        ) {
                                            val piece = viewModel.getPiece(i, j)
                                            val steps = viewModel.getAvailableSteps(piece)

                                            Log.d("BoardStep", "i: ${piece.i}, j: ${piece.j}")

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
            DrawPlayer(viewModel.getCurrentPlayer().toString())
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

    @Composable
    fun ExchangePieceAlert(viewModel: BoardViewModel, isWhiteExchange: Boolean, isBlackExchange: Boolean,onDismiss: () -> Unit){
        if(isWhiteExchange || isBlackExchange){
            AlertDialog(
                onDismissRequest = onDismiss,
                text = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.Black,
                        text = "Choose a piece!",
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                buttons = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 0.dp, 30.dp, 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row{
                            Column{
                                OutlinedButton(
                                    modifier = Modifier.size(70.dp),
                                    onClick = { viewModel.board.value?.exchangePawn(PieceName.QUEEN) },
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    if(isWhiteExchange){
                                        Image(painterResource(id = R.drawable.white_queen), contentDescription = "White bishop")
                                    }
                                    if(isBlackExchange){
                                        Image(painterResource(id = R.drawable.black_queen), contentDescription = "Black bishop")
                                    }
                                }
                                Spacer(Modifier.height(50.dp))
                                OutlinedButton(
                                    modifier = Modifier.size(70.dp),
                                    onClick = { viewModel.board.value?.exchangePawn(PieceName.ROOK) },
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    if(isWhiteExchange){
                                        Image(painterResource(id = R.drawable.white_rook), contentDescription = "White bishop")
                                    }
                                    if(isBlackExchange){
                                        Image(painterResource(id = R.drawable.black_rook), contentDescription = "Black bishop")
                                    }
                                }
                            }
                            Spacer(Modifier.width(50.dp))
                            Column{
                                OutlinedButton(
                                    modifier = Modifier.size(70.dp),
                                    onClick = { viewModel.board.value?.exchangePawn(PieceName.BISHOP) },
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    if(isWhiteExchange){
                                        Image(painterResource(id = R.drawable.white_bishop), contentDescription = "White bishop")
                                    }
                                    if(isBlackExchange){
                                        Image(painterResource(id = R.drawable.black_bishop), contentDescription = "Black bishop")
                                    }
                                }
                                Spacer(Modifier.height(50.dp))
                                OutlinedButton(
                                    modifier = Modifier.size(70.dp),
                                    onClick = { viewModel.board.value?.exchangePawn(PieceName.KNIGHT) },
                                    border = BorderStroke(1.dp, Color.Black),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                                ) {
                                    if(isWhiteExchange){
                                        Image(painterResource(id = R.drawable.white_knight), contentDescription = "White knight")
                                    }
                                    if(isBlackExchange){
                                        Image(painterResource(id = R.drawable.black_knight), contentDescription = "Black knight")
                                    }
                                }
                            }
                        }
                    }
                }

            )
        }
    }
}
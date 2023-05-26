package hu.bme.aut.android.monkeychess.board

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceName

class BoardUI() {
    @Composable
    fun GameScreen(playerOne: String = "Alice", playerTwo: String = "Bob", viewModel :BoardViewModel) {
        val whiteExchange by viewModel.getWhiteExchangeState().observeAsState()
        val blackExchange by viewModel.getBlackExchangeState().observeAsState()
        val boardState by viewModel.board.observeAsState()
        val playerOneMulti by viewModel.playerOne.observeAsState()
        val playerTwoMulti by viewModel.playerTwo.observeAsState()
        val playerOneSingle by viewModel.currentUser.observeAsState()
        val singlePlayerProfilePicture by viewModel.currentUserProfilePicture.observeAsState()
        val multiPlayerProfilePicturePlayerOne by viewModel.playerOneImage.observeAsState()
        val multiPlayerProfilePicturePlayerTwo by viewModel.playerTwoImage.observeAsState()

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


                //PlayerTwo
                Row(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    if(viewModel.isMulti){
                        DrawPlayer(playerTwoMulti.toString(), R.drawable.baseline_person_24, multiPlayerProfilePicturePlayerTwo)
                    }
                    else if (viewModel.doAi){
                        DrawPlayer("Robot", R.drawable.robot, null)
                    }
                    else{
                        DrawPlayer("Guest", R.drawable.baseline_person_24, null)
                    }
                }

                //ChessBoard
                Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    boardState?.let { board ->
                        DrawBoard(board, viewModel)
                    }
                }

                //PlayerOne
                Box(modifier = Modifier.border(width = 1.dp, color = Color.Black)) {
                    if(viewModel.isMulti){
                        DrawPlayer(playerOneMulti.toString(), R.drawable.baseline_person_24, multiPlayerProfilePicturePlayerOne)
                    }
                    else if(viewModel.doAi){
                        DrawPlayer(playerOneSingle.toString(), R.drawable.baseline_person_24, singlePlayerProfilePicture)
                    }
                    else{
                       DrawPlayer(playerOneSingle.toString(), R.drawable.baseline_person_24, singlePlayerProfilePicture)
                    }
                }


                //if(viewModel.isMulti){
                    Button(onClick = { viewModel.board.value?.FlipTheTable() }) {
                        Text(text = "flipy flopity\nyou are my flipity ")
                    }
              //  }

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
    fun DrawPlayer(name: String = "PlayerName", pictureID: Int, pictureRef: String?) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureRef ?: pictureID)
                    .build(),
                loading = {
                    CircularProgressIndicator()
                },
                contentDescription = "Profile picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
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
    fun DrawBoard(board: Board, viewModel: BoardViewModel) {
        Column {
            for (i in 0 until 8) {
                Row {
                    for (j in 0 until 8) {
                        var gridcolor = Color(0xff769656)
                        if ((i + j) % 2 == 0) {
                            gridcolor = Color(0xffeeeedd)
                        }
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(gridcolor)
                                .clickable {

                                    if (board.getValue(i, j) == true) {
                                        viewModel.HideAvailableSteps()
                                        viewModel.step(viewModel.getClickedPiece(), i, j)
                                        //viewModel.ChangeCurrentPlayer()
                                    } else {
                                        viewModel.HideAvailableSteps()
                                        viewModel.setClickedPiece(board.getPiece(i, j))

                                        Log.d(
                                            "Board1",
                                            "i: ${i}, j: ${j} board value: ${
                                                viewModel.getValue(i, j)
                                            } babu:${viewModel.getPiece(i, j).pieceColor} "
                                        )

                                        if (board.getPiece(i, j).pieceColor != PieceColor.EMPTY) {
                                            val piece = board.getPiece(i, j)
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
                                }
                        ) {
                            if (board.getPiece(i, j).pieceColor != PieceColor.EMPTY) {
                                DrawPiece(board.getPiece(i, j).imageID)
                            }
                            if (viewModel.getValue(i, j) == true) {
                                DrawCircle()
                            }
                        }
                    }
                }
            }
            //DrawPlayer(viewModel.getCurrentPlayer().toString(),)
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

            val radius = size.minDimension / 2
            drawCircle(
                color = Color(0xffb3b3b3),
                center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                radius = radius,
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
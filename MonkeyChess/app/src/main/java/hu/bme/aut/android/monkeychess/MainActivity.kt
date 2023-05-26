package hu.bme.aut.android.monkeychess

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.android.monkeychess.welcomeScreen.WelcomeUI
import hu.bme.aut.android.monkeychess.login.LoginUI
import hu.bme.aut.android.monkeychess.login.LoginViewModel
import hu.bme.aut.android.monkeychess.mainMenu.MainMenuUI
import hu.bme.aut.android.monkeychess.register.RegisterUI
import hu.bme.aut.android.monkeychess.register.RegisterViewModel
import hu.bme.aut.android.monkeychess.splashScreen.SplashScreenUI
import hu.bme.aut.android.monkeychess.board.BoardUI
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.board.multi.Multiplayer
import hu.bme.aut.android.monkeychess.board.multi.choose_opponent.ChooseOpponentScreen
import hu.bme.aut.android.monkeychess.board.multi.choose_opponent.ChooseOpponentViewModel
import hu.bme.aut.android.monkeychess.board.multi.select_game.SelectGameScreen
import hu.bme.aut.android.monkeychess.board.multi.select_game.SelectGameViewModel
import hu.bme.aut.android.monkeychess.board.pieces.enums.PieceColor
import hu.bme.aut.android.monkeychess.board.single.SinglePlayer
import hu.bme.aut.android.monkeychess.forgottenPassword.ForgottenPassUI
import hu.bme.aut.android.monkeychess.forgottenPassword.ForgottenPassViewModel
import hu.bme.aut.android.monkeychess.mainMenu.MainMenuViewModel
import hu.bme.aut.android.monkeychess.profile.ProfileUI
import hu.bme.aut.android.monkeychess.profile.ProfileViewModel
import hu.bme.aut.android.monkeychess.profile.modify.email.EmailChangeUI
import hu.bme.aut.android.monkeychess.profile.modify.email.EmailChangeViewModel
import hu.bme.aut.android.monkeychess.profile.modify.password.PasswordChangeUI
import hu.bme.aut.android.monkeychess.profile.modify.password.PasswordChangeViewModel
import hu.bme.aut.android.monkeychess.profile.modify.profileData.ChangeProfileDataUI
import hu.bme.aut.android.monkeychess.profile.modify.profileData.ChangeProfileDataViewModel
import hu.bme.aut.android.monkeychess.ui.theme.MonkeChessTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonkeChessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    MainActivityNavigation()
                }
            }
        }
    }

    @Composable
    fun MainActivityNavigation(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash_screen"){
            composable("splash_screen"){
                TopAppBarWidget(navController = navController, content =  { SplashScreenUI().SplashScreen(navController)})
            }
            composable("welcome_screen"){
                TopAppBarWidget(navController = navController, content =  { WelcomeUI().WelcomeScreenContent(navController)}, bottomBar = {WelcomeUI().WelcomeScreenButtons(navController)})
                BackHandler {
                   Lifecycle.Event.ON_RESUME
                }
            }
            composable("login_screen"){
                val viewModel = LoginViewModel()
                TopAppBarWidget(navController = navController, content =  { LoginUI().LoginScreenContent(viewModel)},bottomBar = { LoginUI().LoginButton(navController, viewModel)} )
                BackHandler {
                    navController.navigate("welcome_screen")
                }
            }
            composable("forgottenPass_screen"){
                val viewModel = ForgottenPassViewModel()
                TopAppBarWidget(navController = navController, content = { ForgottenPassUI().ForgottenPasswordScreen(navController, viewModel)}, bottomBar = {})
            }
            composable("register_screen"){
                val viewModel = RegisterViewModel()
                TopAppBarWidget(navController = navController, content = { RegisterUI().RegisterScreenContent(viewModel)}, bottomBar = { RegisterUI().RegisterButton(navController, viewModel)})
                BackHandler {
                    navController.navigate("welcome_screen")
                }
            }
            composable("MainMenu_screen"){
                val viewModel = MainMenuViewModel()
                TopAppBarWidget(navController = navController, content = { MainMenuUI().MainMenu(navController, viewModel)}, bottomBar = {})
                BackHandler {
                    Lifecycle.Event.ON_RESUME
                }
            }
            composable("board_screen"){
                val singleplayer = remember {
                    SinglePlayer()
                }
                
                val viewModel =BoardViewModel(singleplayer,null,true, PieceColor.BLACK)
                
                TopAppBarWidget(navController = navController, content = { BoardUI().GameScreen(viewModel = viewModel)}, bottomBar = {})
            }
            composable("board_1v1"){
                val singleplayer = remember {
                    SinglePlayer()
                }
                val viewModel =BoardViewModel(singleplayer,null, false, PieceColor.EMPTY)
                TopAppBarWidget(navController = navController, content = { BoardUI().GameScreen(viewModel = viewModel)}, bottomBar = {})
            }

            composable("profile_screen"){
                val viewModel = ProfileViewModel()
                TopAppBarWidget(navController = navController, content = { ProfileUI().ProfileScreen(viewModel, navController = navController) }, bottomBar = {})
                BackHandler {
                    navController.navigate("MainMenu_screen")
                }
            }
            composable("change_email"){
                val viewModel = EmailChangeViewModel()
                TopAppBarWidget(navController = navController, content = { EmailChangeUI().EmailChangeScreen(navController = navController, viewModel = viewModel)}, bottomBar = {})
            }
            composable("change_password"){
                val viewModel = PasswordChangeViewModel()
                TopAppBarWidget(navController = navController, content = { PasswordChangeUI().PasswordChangeScreen(navController = navController, viewModel = viewModel)}, bottomBar = {})
            }
            composable("change_profile_data"){
                val viewModel = ChangeProfileDataViewModel()
                TopAppBarWidget(navController = navController, content = {ChangeProfileDataUI().ChangeProfileDataScreen(viewModel, navController)}, bottomBar = {})
            }
            composable("select_game"){
                val viewModel = SelectGameViewModel()
                TopAppBarWidget(navController = navController, content = { SelectGameScreen(navController = navController, viewModel = viewModel) }, bottomBar = {})
                BackHandler {
                    navController.navigate("MainMenu_screen")
                }
            }
            composable("choose_opponent"){
                val viewModel = ChooseOpponentViewModel()
                TopAppBarWidget(navController = navController, content = { ChooseOpponentScreen(navController = navController, viewModel = viewModel) }, bottomBar = {})
            }

            composable("new_multiplayer_game/{player1}/{player2}"){ backStackEntry ->
                val player1 = backStackEntry.arguments?.getString("player1")
                val player2 = backStackEntry.arguments?.getString("player2")

                val singleplayer = remember {
                    SinglePlayer()
                }

                val multiplayer = remember {
                    Multiplayer(
                        playerOne = player1.toString(),
                        playerTwo = player2.toString(),
                        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq",
                        gameId = "",
                        isNewGame = true
                    )
                }

                val viewModel = remember {
                    BoardViewModel(singleplayer, multiplayer, false, PieceColor.EMPTY)
                }

                TopAppBarWidget(
                    navController = navController,
                    content = {
                        BoardUI().GameScreen(viewModel = viewModel)
                    },
                    bottomBar = {}
                )

                BackHandler {
                    navController.navigate("select_game")
                }
            }

            composable("load_multiplayer_game/{playerOne}/{playerTwo}/{gameID}/{fen}") { backStackEntry ->
                val player1 = backStackEntry.arguments?.getString("playerOne")
                val player2 = backStackEntry.arguments?.getString("playerTwo")
                val gameID = backStackEntry.arguments?.getString("gameID")
                val fen = Uri.decode(backStackEntry.arguments?.getString("fen"))

                val singleplayer = remember {
                    SinglePlayer()
                }

                val multiplayer = remember {
                    Multiplayer(
                        playerOne = player1.toString(),
                        playerTwo = player2.toString(),
                        gameId = gameID.toString(),
                        fen = fen.toString(),
                        isNewGame = false
                    )
                }

                val viewModel = remember {
                    BoardViewModel(singleplayer, multiplayer, false, PieceColor.EMPTY)
                }

                TopAppBarWidget(navController = navController, content = { BoardUI().GameScreen(viewModel = viewModel) }, bottomBar = {})
                BackHandler { navController.navigate("select_game") }
            }
        }
    }


    /**
     * TopAppBar
     * */
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun TopAppBarWidget(navController: NavController, content: @Composable () -> Unit={}, bottomBar: @Composable () -> Unit={} ) {
        Scaffold(
            topBar = {WelcomeScreenTopBar() },
            content = {content()},
            bottomBar = {bottomBar()}
        )
    }

    @Composable
    fun WelcomeScreenTopBar(){
        TopAppBar(
            title = {
                Text(
                    text = "MonkeyChess",
                    color = MaterialTheme.colors.background
                )
            },
            backgroundColor = MaterialTheme.colors.onBackground,
            contentColor = MaterialTheme.colors.background
        )
    }
}


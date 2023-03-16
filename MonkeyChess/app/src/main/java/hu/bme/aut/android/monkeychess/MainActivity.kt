package hu.bme.aut.android.monkeychess

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.monkeychess.welcomeScreen.WelcomeUI
import hu.bme.aut.android.monkeychess.login.LoginUI
import hu.bme.aut.android.monkeychess.login.LoginViewModel
import hu.bme.aut.android.monkeychess.mainMenu.MainMenuUI
import hu.bme.aut.android.monkeychess.register.RegisterUI
import hu.bme.aut.android.monkeychess.register.RegisterViewModel
import hu.bme.aut.android.monkeychess.splashScreen.SplashScreenUI
import hu.bme.aut.android.monkeychess.board.BoardUI
import hu.bme.aut.android.monkeychess.board.BoardViewModel
import hu.bme.aut.android.monkeychess.profile.ProfileUI
import hu.bme.aut.android.monkeychess.profile.ProfileViewModel
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
            composable("register_screen"){
                val viewModel = RegisterViewModel()
                TopAppBarWidget(navController = navController, content = { RegisterUI().RegisterScreenContent(viewModel)}, bottomBar = { RegisterUI().RegisterButton(navController, viewModel)})
                BackHandler {
                    navController.navigate("welcome_screen")
                }
            }
            composable("MainMenu_screen"){
                TopAppBarWidget(navController = navController, content = { MainMenuUI().MainMenu(navController)}, bottomBar = {})
                BackHandler {
                    Lifecycle.Event.ON_RESUME
                }
            }
            composable("board_screen"){
                val viewModel =BoardViewModel()
                TopAppBarWidget(navController = navController, content = { BoardUI().GameScreen(viewModel = viewModel)}, bottomBar = {})
            }
            composable("profile_screen"){
                val viewModel = ProfileViewModel()
                TopAppBarWidget(navController = navController, content = { ProfileUI().ProfileScreen(viewModel) }, bottomBar = {})
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

